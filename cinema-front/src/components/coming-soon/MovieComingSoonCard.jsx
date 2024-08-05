import React, { useContext } from "react"
import { Card, Col } from "react-bootstrap"
import { Link } from "react-router-dom"

const MovieComingSoonCard = ({movieComingSoon}) => {

    const {id, name, duration, distributor, country, year, description, releaseDate, photo} = movieComingSoon;

    return(
        <Col key={movieComingSoon.id} className="mb-4" xs={12}>
            <Card>
                <Card.Body className="d-flex flex-wrap align-items-center">
                    <div className="flex-shrrink-0 mr-3 mb-3 mb-md-0">
                        <Link to={`/buy-ticket/${movieComingSoon.id}`}>
                            <Card.Img
								variant="top"
								src={`data:image/png;base64, ${movieComingSoon.photo}`}
								alt="Movie Photo"
								style={{ width: "100%", maxWidth: "200px", height: "auto" }}
							/>
                        </Link>
                    </div>
                    <div className="flex-grow-1 ml-3 px-5">
                        <Card.Title className="hotel-color">Name: {movieComingSoon.name}</Card.Title>
                        <Card.Title className="hotel-color">Duration: {movieComingSoon.duration} / minutes</Card.Title>
                        <Card.Title className="hotel-color">Distributor: {movieComingSoon.distributor}</Card.Title>
                        <Card.Title className="hotel-color">Country: {movieComingSoon.country}</Card.Title>
                        <Card.Title className="hotel-color">Year: {movieComingSoon.year}</Card.Title>
                        <Card.Title className="hotel-color"> 
                            Release-date: {releaseDate ? new Date(releaseDate[0], releaseDate[1] - 1, releaseDate[2]).toLocaleString() : "N/A"}
                        </Card.Title>
                        
                    </div>
                    <div className="flex-shrink-0 mt-3">
                        <Link to={`/go-to-details/${movieComingSoon.id}`} className="btn btn-hotel btn-sm">
                            Details
                        </Link>
                    </div>
                </Card.Body>
            </Card>
        </Col>
    );

};

export default MovieComingSoonCard;