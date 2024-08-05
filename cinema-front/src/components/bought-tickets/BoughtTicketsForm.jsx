import React, { useEffect } from "react"
import moment from "moment"
import { useState } from "react"
import { Form, FormControl, Button } from "react-bootstrap"
import { useNavigate, useParams } from "react-router-dom"
import { getProjectionById, buyTicket,
     getAllProjections, getAllHalls, getAvailableSeats, getAvailableSeatsByHallId, countByTicketsProjectionId } 
     from "../utils/AppFunction"
import BoughtTicketsSummary from "./BoughtTicketsSummary";

const BoughtTicketsForm = () => {

    const [validated, setValidated] = useState(false)
	const [isSubmitted, setIsSubmitted] = useState(false)
	const [errorMessage, setErrorMessage] = useState("")
	//const [ticketPrice, setTicketPrice] = useState(0)
    const [projections, setProjections] = useState([]);
    const [seats, setSeats] = useState([]);
    const [halls, setHalls] = useState([]);
    const [selectedHall, setSelectedHall] = useState("");
    const [selectedSeatNumber, setSelectedSeatNumber] = useState("");
    const [projectionPrice, setProjectionPrice] = useState(0);
    const [ticketCount, setTicketCount] = useState(0);
    const TicketStatus = {
        CANCELLED: "CANCELLED",
        RESERVED: "RESERVED",
        PURCHASED: "PURCHASED",
        AVAILABLE: "AVAILABLE"
    };

    const currentUser = localStorage.getItem("userId")
    const { projectionId } = useParams()
	const navigate = useNavigate()
    const[tickets, setTickets] = useState({
        projection:"",
        seatNumber:"",
        status:TicketStatus,
        user:currentUser,
        email:"",
        quantity:1
    });

    useEffect(() => {
        async function fetchData() {
          try {
            const projectionsData = await getAllProjections();
            setProjections(projectionsData);
    
            const hallsData = await getAllHalls();
            setHalls(hallsData);
          } catch (error) {
            console.error("Error fetching data", error);
          }
        }
        fetchData();
      }, []);

    const fetchProjection = async (projectionId) => {
        console.log("Fetching projection with ID:", projectionId);
        try {
            const data = await getProjectionById(projectionId);
            console.log("Projection Data:", data);
            setProjectionPrice(data.projectionPrice);
            setTickets(prevTickets => ({
                ...prevTickets,
                price: data.projectionPrice
            }));
        } catch (error) {
            console.error("Error fetching projection:", error);
        }
    };  

    useEffect(() => {
        console.log("Projection ID from URL:", projectionId);
        if(projectionId) {
          getProjectionPriceById(projectionId);
          fetchAvailableSeats(projectionId);
          fetchTicketCount(projectionId);
          fetchProjection(projectionId)
        }
      }, [projectionId]);
    
    useEffect(() => {
        console.log("Projection Price:", projectionPrice);
        console.log("Calculated Payment:", calculatePayment());
    }, [projectionPrice, tickets.quantity]);
    
    console.log("Tickets State:", tickets); 

    const fetchTicketCount = async(projectionId) => {
        try{
            const count = await countByTicketsProjectionId(projectionId)
            setTicketCount(count);
        }
        catch(error){
            console.error("Error fetching tickets count ", error);
        }
    };  

    const fetchAvailableSeats = async (projectionId) => {
        try {
          const seatsData = await getAvailableSeats(projectionId);
          setSeats(seatsData);
        } catch (error) {
          console.error("Error fetching seats", error);
        }
    };  

    useEffect(() => {
        if (selectedHall) {
            async function fetchSeats() {
                try {
                    const seatsData = await getAvailableSeats(selectedHall);
                    setSeats(seatsData);
                } catch (error) {
                    console.error("Error fetching seats", error);
                }
            }
            fetchSeats();
        }
    }, [selectedHall]);

    const handleSeatChange = (e) => {
        const selectedSeat = e.target.value;
        setSelectedSeatNumber(selectedSeat);
        setTickets(prevTickets => ({
            ...prevTickets,
            seatNumber: selectedSeat
        }));
    };

    /*const handleInputChange = (e) => {
        const { name, value } = e.target;
        // Ako je `name` saleDateTime, pretvara value u ISO format
        if (name === "saleDateTime") {
            const date = moment(value).format("YYYY-MM-DDTHH:mm:ss"); // formatiranje u ISO format
            setTickets({ ...tickets, [name]: date });
        } 
        else {
            const parsedValue = (name === "quantity") ? parseFloat(value) : value;
            setTickets({ ...tickets, [name]: isNaN(parsedValue) ? 0 : parsedValue });
        }
        setErrorMessage("");
    };*/
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        if (name === "quantity") {
            setTickets(prevTickets => ({
                ...prevTickets,
                [name]: parseInt(value) || 1
            }));
        } else if (name === "status") {
            setTickets(prevTickets => ({
                ...prevTickets,
                status: value
            }));
        } else {
            setTickets(prevTickets => ({
                ...prevTickets,
                [name]: value
            }));
        }
        setErrorMessage("");
    };

    const handleHallChange = (e) => {
        setSelectedHall(e.target.value);
        setTickets({ ...tickets, seat: "" }); // Resetovanje izabranog sedista kada se promeni sala
      };

    const getProjectionPriceById = async(projectionId) =>{
        try{
            const response = await getProjectionById(projectionId);
            setProjectionPrice(response.projectionPrice);
        }
        catch (error) {
			throw new Error(error)
		}
    }

    const calculatePayment = () => {
        const ticketPrice = projectionPrice;
        const numOfTickets = tickets.quantity || 1;
        const totalPayment = ticketPrice * numOfTickets;
        console.log("Total Payment:", totalPayment); 
        return totalPayment;
    };

    const submitTicket = async () => {
        const ticketRequest = {
            ...tickets,
            seatNumber: selectedSeatNumber,
            status: tickets.status
        };
        try {
            const confirmationCode = await buyTicket(projectionId, ticketRequest, tickets.status);
            setIsSubmitted(true);
            navigate("/bought-ticket-success", { state: { message: confirmationCode } });
        } catch (error) {
            console.error("Error buying ticket", error);
            setErrorMessage("Error buying ticket. Please try again.");
            //navigate("/bought-ticket-success", { state: { message: confirmationCode } });
        }
    }; 
    
    const handleSubmit = async (e) => {
        e.preventDefault();
        const totalPayment = calculatePayment();
        console.log("Total Payment to be processed:", totalPayment);
        const form = e.currentTarget;
        if (form.checkValidity() === false) {
            e.stopPropagation();
        } else {
            setIsSubmitted(true);
            await submitTicket();
        }
        setValidated(true);
    };

    return(
        <>
        <div className="container mb-5">
            <div className="row">
                <div className="col-md-6">
                    <div className="card card-body mt-5">
                        <h4 className="card-title">Buy Ticket</h4>
                        <Form onSubmit={handleSubmit} noValidate validated={validated}>
                            <Form.Group>
                                <Form.Label>Projection</Form.Label>
                                <Form.Select
                                    required
                                    id="projection"
                                    name="projection"
                                    value={tickets.projection}
                                    onChange={handleInputChange}
                                    >
                                    <option value="">Choose your projection</option>
                                    {projections.map((projection) => (
                                        <option key={projection.id} value={projection.id}>
                                        {projection.movieProjectionResponse ? projection.movieProjectionResponse.name : "No Movie"} - {moment(projection.dateTime).format("MMMM Do YYYY, h:mm:ss a")}
                                        </option>
                                    ))}
                                </Form.Select>
                                <Form.Control.Feedback type="invalid">
										Please choose your projection.
								</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group>
                                <Form.Label>Hall</Form.Label>
                                <Form.Select
                                    required
                                    id="hall"
                                    name="hall"
                                    value={selectedHall}
                                    onChange={handleHallChange}
                                >
                                    <option value="">Choose your hall</option>
                                    {halls.map((hall) => (
                                    <option key={hall.id} value={hall.id}>
                                        {hall.name}
                                    </option>
                                    ))}
                                </Form.Select>
                                <Form.Control.Feedback type="invalid">
                                    Please choose your hall.
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group>
                                <Form.Label>Seat</Form.Label>
                                <Form.Select
                                    required
                                    type="number"
                                    id="seat"
                                    name="seat"
                                    value={selectedSeatNumber}
                                    onChange={handleSeatChange}
                                >
                                <option value="">Choose your seat</option>
                                    {seats.map((seat) => (
                                        <option key={seat.id} value={seat.seatNumber}>
                                            Seat {seat.seatNumber} 
                                        </option>
                                    ))}
                                </Form.Select>
                                <Form.Control.Feedback type="invalid">
                                    Please choose your seat.
                                </Form.Control.Feedback>
                            </Form.Group> 
                            
                            <Form.Group>
                                <Form.Label>Ticket Status</Form.Label>
                                <Form.Select
                                required
                                id="status"
                                name="status"
                                value={tickets.status}
                                onChange={handleInputChange}
                                >
                                    <option value="">Choose ticket status</option>
                                    {Object.values(TicketStatus).map((status) => (
                                        <option key={status} value={status}>{status}</option>
                                    ))}
                                </Form.Select>
                                <Form.Control.Feedback type="invalid">
									Please choose ticket status.
								</Form.Control.Feedback>
                            </Form.Group>
                            <Form.Group>
								<Form.Label htmlFor="email" className="hotel-color">
									Email
								</Form.Label>
								<FormControl
                                    required
                                    type="email"
                                    id="email"
                                    name="email"
                                    value={tickets.email || ""}
                                    placeholder="Enter your email"
                                    onChange={handleInputChange}
                                />
								<Form.Control.Feedback type="invalid">
									Please enter a valid email address.
								</Form.Control.Feedback>
							</Form.Group>
                            <Form.Group>
                                <Form.Label>Quantity</Form.Label>
                                <Form.Control
                                    type="number"
                                    id="quantity"
                                    name="quantity"
                                    value={tickets.quantity || 1}
                                    min="1"
                                    onChange={handleInputChange}
                                />
                                <Form.Control.Feedback type="invalid">
                                    Please enter a valid quantity.
                                </Form.Control.Feedback>
                            </Form.Group>  
                            <div className="form-group mt-2 mb-2">
								<button type="submit" className="btn btn-hotel">
									Continue
								</button>
							</div>
                        </Form>
                        {errorMessage && <p className="error-message text-danger">{errorMessage}</p>}
                    </div>
                </div>
                <div className="col-md-4">
                    {isSubmitted && (
                        <BoughtTicketsSummary 
                        buying={tickets}
                        payment={calculatePayment()}
                        isFormValid={ validated}
                        onConfirm={submitTicket}
                        />
                    )}
                </div>
            </div>
        </div>
        </>
    );
};

export default BoughtTicketsForm;

