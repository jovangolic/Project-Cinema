import React, { useContext, useState, useEffect } from "react"
import { Card, Col, Container } from "react-bootstrap"
import { Link, useParams } from "react-router-dom"
import { getMovieDetails } from "../utils/AppFunction";

const MovieComingSoonDetails = () => {

    const { movieComingSoonId } = useParams();
    const [movieDetails, setMovieDetails] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        const fetchMovieDetails = async () => {
            console.log("movieComingSoonId:", movieComingSoonId);
            setIsLoading(true);
            try {
                const result = await getMovieDetails(movieComingSoonId);
                setMovieDetails(result);
                setIsLoading(false);
            } catch (error) {
                setErrorMessage("Error fetching movie details");
                setIsLoading(false);
            }
        };

        if (movieComingSoonId) {
            fetchMovieDetails();
        }
    }, [movieComingSoonId]);

    if (isLoading) {
        return <p>Loading movie details...</p>;
    }

    if (errorMessage) {
        return <p>{errorMessage}</p>;
    }

    return (
        <Container className="mt-5">
            {movieDetails && (
                <Card>
                    <Card.Body>
                        <Card.Img
                            variant="top"
                            src={`data:image/png;base64, ${movieDetails.photo}`}
                            alt="Movie Photo"
                            style={{ width: "100%", maxWidth: "400px", height: "auto" }}
                            className="mb-3"
                        />
                        <Card.Title className="hotel-color">Name: {movieDetails.name}</Card.Title>
                        <Card.Text>Duration: {movieDetails.duration} minutes</Card.Text>
                        <Card.Text>Distributor: {movieDetails.distributor}</Card.Text>
                        <Card.Text>Country: {movieDetails.country}</Card.Text>
                        <Card.Text>Year: {movieDetails.year}</Card.Text>
                        <Card.Text>Release Date: {movieDetails.releaseDate ? new Date(movieDetails.releaseDate[0], movieDetails.releaseDate[1] - 1, movieDetails.releaseDate[2]).toLocaleDateString() : "N/A"}</Card.Text>
                        <Card.Text style={{ marginTop: '20px', fontSize: '1.5em', fontWeight: 'bold' }}>
                            Description
                        </Card.Text>
                        <div style={{ border: '1px solid #ccc', padding: '10px', borderRadius: '5px', backgroundColor: '#f9f9f9' }}>
                            <Card.Text style={{ marginTop: '10px', fontSize: '1.2em' }}>
                                {movieDetails.description}
                            </Card.Text>
                        </div>
                        <div className="flex-shrink-0 mt-3">
                        <Link to={`/coming-soon`} className="btn btn-hotel btn-sm">
                            Back
                        </Link>
                    </div>
                    </Card.Body>
                </Card>
            )}
        </Container>
    );

};

export default MovieComingSoonDetails;