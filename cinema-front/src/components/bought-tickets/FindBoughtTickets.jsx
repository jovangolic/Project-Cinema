import React, { useState } from "react"
import moment from "moment"
import { deleteTicket, findByTicketConfirmationCode } from "../utils/AppFunction";

const FindBoughtTickets = () => {

    const [confirmationCode, setConfirmationCode] = useState("")
	const [error, setError] = useState(null)
	const [successMessage, setSuccessMessage] = useState("")
	const [isLoading, setIsLoading] = useState(false)
    const [isDeleted, setIsDeleted] = useState(false)
    const [tickets, setTickets] = useState([]);
    const TicketStatus = {
        CANCELLED: "CANCELLED",
        RESERVED: "RESERVED",
        PURCHASED: "PURCHASED",
        AVAILABLE: "AVAILABLE"
    };
    const [ticketInfo, setTicketInfo] = useState({
        id: "",
        projectionTicketResponse: {
            id: "",
            movieResponse: { name: "" },
            typeResponse: { name: "" },
            hallResponse: { name: "" },
            dateTime: [],
            ticketPrice: ""
        },
        seatResponse: { seatNumber: "" },
        saleDateTime: [],
        status: "",
        userDto: { email: "" }
    });
    
    const emptyTicketInfo = {
        id: "",
        projectionTicketResponse: {
            id: "",
            movieResponse: { name: "" },
            typeResponse: { name: "" },
            hallResponse: { name: "" },
            dateTime: [],
            ticketPrice: ""
        },
        seatResponse: { seatNumber: "" },
        saleDateTime: [],
        status: "",
        userDto: { email: "" }
    };

	const handleInputChange = (event) => {
		setConfirmationCode(event.target.value)
	}

    const handleFormSubmit = async(event) => {
        event.preventDefault()
		setIsLoading(true)
        try {
            const data = await findByTicketConfirmationCode(confirmationCode);
            console.log("Data from backend:", data);
            //setTicketInfo(data);
            setTickets(data)
            setError(null);
        } catch (error) {
            console.error("Error fetching ticket:", error);
            //setTicketInfo(emptyTicketInfo);
            setTickets([])
            if (error.response && error.response.status === 404) {
                setError(error.response.data.message);
            } else {
                setError(error.message);
            }
        }
        setTimeout(() => setIsLoading(false), 2000);
    };

    const handleBuyingCancellation = async(ticketId) => {
        try{
            await deleteTicket(ticketId)
            setIsDeleted(true)
			setSuccessMessage("Buying has been cancelled successfully!")
			//setTicketInfo(emptyTicketInfo);
            setTickets(tickets.filter(ticket => ticket.id !== ticketId));
			setConfirmationCode("")
			setError(null)
        }
        catch (error) {
			setError(error.message)
		}
		setTimeout(() => {
			setSuccessMessage("")
			setIsDeleted(false)
		}, 2000)
    };

    return (
        <>
            <div className="container mt-5 d-flex flex-column justify-content-center align-items-center">
                <h2 className="text-center mb-4">Find My Bought-Tickets</h2>
                <form onSubmit={handleFormSubmit} className="col-md-6">
                    <div className="input-group mb-3">
                        <input
                            className="form-control"
                            type="text"
                            id="confirmationCode"
                            name="confirmationCode"
                            value={confirmationCode}
                            onChange={handleInputChange}
                            placeholder="Enter the bought-ticket confirmation code"
                        />
                        <button type="submit" className="btn btn-hotel input-group-text">
                            Find bought-ticket
                        </button>
                    </div>
                </form>
                {isLoading ? (
                    <div>Finding your bought-tickets...</div>
                ) : error ? (
                    <div className="text-danger">Error: {error}</div>
                ) : tickets.length > 0 ? (
                    tickets.map(ticket => (
                        <div className="col-md-6 mt-4 mb-5" key={ticket.id}>
                            <h3>Ticket Information</h3>
                            <p className="text-success">Ticket Number: {ticket.id}</p>
                            <p>Projection Number: {ticket.projectionTicketResponse.id}</p>
                            <p>Movie: {ticket.projectionTicketResponse.movieResponse.name}</p>
                            <p>Projection Type: {ticket.projectionTicketResponse.typeResponse.name}</p>
                            <p>Hall: {ticket.projectionTicketResponse.hallResponse.name}</p>
                            <p>Seat Number: {ticket.seatResponse.seatNumber}</p>
                            <p>Sale Date: {moment(ticket.saleDateTime).subtract(1, "month").format("YYYY-MM-DD HH:mm")}</p>
                            <p>Price: {ticket.projectionTicketResponse.ticketPrice} RSD</p>
                            <p>Ticket Status: {ticket.status}</p>
                            <p>Email: {ticket.userDto.email}</p>
                            {!isDeleted && (
                                <button
                                    onClick={() => handleBuyingCancellation(ticket.id)}
                                    className="btn btn-danger">
                                    Cancel Ticket
                                </button>
                            )}
                        </div>
                    ))
                ) : (
                    <div>find ticket...</div>
                )}
                {isDeleted && <div className="alert alert-success mt-3 fade show">{successMessage}</div>}
            </div>
        </>
    );
};

export default FindBoughtTickets;