import React, { useEffect, useState } from "react"
import { useParams } from "react-router-dom"
import MovieCarousel from "../common/MovieCarousel";
import BoughtTicketsForm from "../bought-tickets/BoughtTicketsForm";
import { getProjectionById, getMovieById } from "../utils/AppFunction";


const Checkout = () => {
    const [error, setError] = useState(null)
	const [isLoading, setIsLoading] = useState(true)
    const [projectionInfo,setProjectionInfo] = useState({
        id:"",
        movieProjectionResponse:""
    });
    
    const { projectionId} = useParams();
    

    useEffect(()=>{
        setTimeout(()=> {
            getProjectionById(projectionId)
            .then((data) => {
                setProjectionInfo(data)
                setIsLoading(false)
            })
            .catch((error) => {
                setError(error.message)
                setIsLoading(false)
            })
        },2000)
    },[projectionId]);

    return(
        <div>
            <section className="container">
                <div className="row">
                    <div className="col-md-4 mt-5 mb-5">
                        {isLoading ? (
                            <p>Loading Projection information...</p>
                        ) : error ? (
                            <p>{error}</p>
                        ) : (
                            <div className="room-info">
                                <p><strong>Projection info:</strong></p>
                                <table className="table table-bordered">
                                    <tbody>
                                        <tr>
                                            <th>Projection ID:</th>
                                            <td>{projectionInfo.id}</td>
                                        </tr>
                                        <tr>
                                            <th>Movie Name:</th>
                                            <td>{projectionInfo.movieProjectionResponse.name}</td>
                                        </tr>
                                        <tr>
                                            <th>Duration:</th>
                                            <td>{projectionInfo.movieProjectionResponse.duration}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        )}
                    </div>
                    <div className="col-md-8">
                        <BoughtTicketsForm />
                    </div>
                </div>
            </section>
            <div className="container">
                <MovieCarousel />
            </div>
        </div>
    );
};

export default Checkout;