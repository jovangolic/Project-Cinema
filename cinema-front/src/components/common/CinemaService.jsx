import React from "react";
import { Card, Col, Container, Row } from "react-bootstrap";
import Header from "./Header";
import { FaWifi, FaCocktail, FaParking, FaSnowflake, FaClock, FaFilm, FaVolumeUp,
	FaFacebook,FaTwitter,FaInstagram
} from "react-icons/fa";
import { GiPopcorn } from "react-icons/gi";


const CinemaService = () => {


    return(
        <>
			<div className="social-icons">
                <a href="https://www.facebook.com" target="_blank" rel="noopener noreferrer"><FaFacebook /></a>
                <a href="https://www.twitter.com" target="_blank" rel="noopener noreferrer"><FaTwitter /></a>
                <a href="https://www.instagram.com" target="_blank" rel="noopener noreferrer"><FaInstagram /></a>
            </div>
            <Container className="mb-2">
                <Header title={"Our Services"} />
                <Row className="justify-content-center">
					<Col md={8}>
						<h4 className="text-center">
							Services at <span className="hotel-color">Awesome-O - Cinema</span>
						</h4>
						<hr />
						<div className="text-center mt-4">
							<h5>Buying tickets at the box office</h5>
							<FaClock className="mr-2" />
							<span className="hotel-color">Box Office Hours: </span>
							11:00 - 22:30
						</div>
                	</Col>
                </Row>
                <hr/>
                <Row xs={1} md={2} lg={3} className="g-4 mt-2">
                    <Col>
                        <Card>
                            <Card.Body>
                                <Card.Title className="hotel-color">
                                    <FaWifi /> WiFi
                                </Card.Title>
                                <Card.Text>Stay connected with high-speed internet access.</Card.Text>
                            </Card.Body>
                        </Card>
                    </Col>

                    
					<Col>
						<Card>
							<Card.Body>
								<Card.Title className="hotel-color">
									<FaCocktail /> Mini-bar
								</Card.Title>
								<Card.Text>Enjoy a refreshing drink or snack from our in-room mini-bar.</Card.Text>
							</Card.Body>
						</Card>
					</Col>
					<Col>
						<Card>
							<Card.Body>
								<Card.Title className="hotel-color">
									<FaParking /> Parking
								</Card.Title>
								<Card.Text>Park your car conveniently in our on-site parking lot.</Card.Text>
							</Card.Body>
						</Card>
					</Col>
					<Col>
						<Card>
							<Card.Body>
								<Card.Title className="hotel-color">
									<FaSnowflake /> Air conditioning
								</Card.Title>
								<Card.Text>Stay cool and comfortable in any halls with our air conditioning system.</Card.Text>
							</Card.Body>
						</Card>
					</Col>

					<Col>
						<Card>
							<Card.Body>
								<Card.Title className="hotel-color">
									<FaVolumeUp /> Dolby surround sound system
								</Card.Title>
								<Card.Text>With surround sound, your entertainment springs to life, putting you in the center of the action. .</Card.Text>
							</Card.Body>
						</Card>
					</Col>

                    <Col>
						<Card>
							<Card.Body>
								<Card.Title className="hotel-color">
                                <GiPopcorn size={50} color="goldenrod" /> Popcorn
								</Card.Title>
								<Card.Text>Enjoy in awesome Popcorn.</Card.Text>
							</Card.Body>
						</Card>
					</Col>

					<Col>
						<Card>
							<Card.Body>
								<Card.Title className="hotel-color">
									<FaFilm /> Movies
								</Card.Title>
								<Card.Text>Relax and enjoy in awesome Movies.</Card.Text>
							</Card.Body>
						</Card>
					</Col>
                </Row>
            </Container>
        </>
    );
};

export default CinemaService;