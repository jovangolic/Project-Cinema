import React, { useContext, useState } from "react"
import { NavLink, Link } from "react-router-dom"
import Logout from "../auth/Logout";
import { FaCalendarAlt } from 'react-icons/fa';


const NavBar = () => {
    const [showAccount, setShowAccount] = useState(false)
	const [showInfo, setShowInfo] = useState(false)

	const handleAccountClick = () => {
		setShowAccount(!showAccount)
	}

	const handleInfoClick = () => {
		setShowInfo(!showInfo)
	};

	const isLoggedIn = localStorage.getItem("token")
	const userRole = localStorage.getItem("userRole")

    return(
        <nav className="navbar navbar-expand-lg bg-body-tertiary px-5 shadow mt-5 sticky-top">
			<div className="container-fluid">
				<Link to={"/"} className="navbar-brand">
					<span className="hotel-color">Awesome-O CINEMA</span>
				</Link>

				<button
					className="navbar-toggler"
					type="button"
					data-bs-toggle="collapse"
					data-bs-target="#navbarScroll"
					aria-controls="navbarScroll"
					aria-expanded="false"
					aria-label="Toggle navigation">
					<span className="navbar-toggler-icon"></span>
				</button>

				<div className="collapse navbar-collapse" id="navbarScroll">
					<ul className="navbar-nav me-auto my-2 my-lg-0 navbar-nav-scroll">
						<li className="nav-item">
							<NavLink className="nav-link" aria-current="page" to={"/browse-all-movies"}>
								Movies
							</NavLink>
						</li>

                        <li className="nav-item">
							<NavLink className="nav-link" aria-current="page" to={"/browse-all-projections"}>
								Projections
							</NavLink>
						</li>

						<li className="nav-item">
							<NavLink className="nav-link" aria-current="page" to={"/coming-soon"}>
								Coming-Soon
							</NavLink>
						</li>

						<li className="nav-item">
							<NavLink className="nav-link" aria-current="page" to={"/browse-all-projections"}>
								<FaCalendarAlt className="mr-2" />
									Reserve Online
							</NavLink>
						</li>

						{isLoggedIn && userRole === "ROLE_ADMIN" && (
							<li className="nav-item">
								<NavLink className="nav-link" aria-current="page" to={"/admin"}>
									Admin
								</NavLink>
							</li>
						)}
					</ul>

					<ul className="d-flex navbar-nav">
						<li className="nav-item dropdown"></li>
					</ul>
					<ul className="d-flex navbar-nav">
						<li className="nav-item dropdown">
							<a 
							className="nav-link dropdown-toggle"
							href="#" 
							id="infoDropdown" 
							role="button" 
							data-bs-toggle="dropdown" 
							aria-expanded="false"
							
							>
							Info
							</a>
							<ul className="dropdown-menu" aria-labelledby="infoDropdown">
							<li>
								<NavLink className="dropdown-item"  to="/about">
									About
								</NavLink>
							</li>
							<li>
								<NavLink className="dropdown-item"  to="/contact">
									Contact
								</NavLink>
							</li>
							</ul>
						</li>
						
    				</ul>

					<ul className="d-flex navbar-nav">
						<li className="nav-item">
							<NavLink className="nav-link" to={"/find-bought-tickets"}>
								Find my tickets
							</NavLink>
						</li>

						<li className="nav-item dropdown">
							<a
								className={`nav-link dropdown-toggle ${showAccount ? "show" : ""}`}
								href="#"
								role="button"
								data-bs-toggle="dropdown"
								aria-expanded="false"
								onClick={handleAccountClick}>
								{" "}
								Account
							</a>

							<ul
								className={`dropdown-menu ${showAccount ? "show" : ""}`}
								aria-labelledby="navbarDropdown">
								{isLoggedIn ? (
									<Logout />
								) : (
									<li>
										<Link className="dropdown-item" to={"/login"}>
											Login
										</Link>
									</li>
								)}
							</ul>
						</li>
					</ul>
				</div>
			</div>
		</nav>
    );
};

export default NavBar;