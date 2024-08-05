import React from "react"
import { useLocation } from "react-router-dom";
import MainHeader from "../layout/MainHeader";
import Parallax from "../common/Parallax";
import CinemaService from "../common/CinemaService";
import MovieCarousel from "../common/MovieCarousel";
import MovieComingSoonCarousel from "../common/MovieComingSoonCarousel";

const Home = () => {
    const location = useLocation()

	const message = location.state && location.state.message
	const currentUser = localStorage.getItem("userId")

    return(
        <section>
            {message && <p className="text-warning px-5">{message}</p>}
			{currentUser && (
				<h6 className="text-success text-center"> You are logged-In as {currentUser}</h6>
			)}
            <MainHeader />
            <div className="container">
                <MovieCarousel />
                <Parallax />
                <MovieCarousel />
                <CinemaService />
                <Parallax />
                <MovieComingSoonCarousel />
            </div>
        </section>
    );
};

export default Home;