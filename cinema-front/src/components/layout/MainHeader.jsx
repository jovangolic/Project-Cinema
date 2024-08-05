import React from "react";

const MainHeader = () => {

    return(
        <header className="header-banner">
			<div className="overlay"></div>
			<div className="animated-texts overlay-content">
				<h1 className="animated-title">
					Welcome to <span className="hotel-color"> Awesone-O Cinema</span>
				</h1>
				<h4 className="animated-subtitle">Experience the Best Movies in City</h4>
				
			</div>
		</header>
    );
};

export default MainHeader;

/*
<NavLink className="nav-link" aria-current="page" to={"/browse-all-projections"}>
	<Button variant="primary" size="lg" className="mr-2">Book Now</Button>
</NavLink>
<NavLink className="nav-link" aria-current="page" to={"/browse-all-projections"}>
	<Button variant="secondary" size="lg">Learn More</Button>
</NavLink>
*/