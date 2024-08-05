import React, { useEffect, useState } from "react"
import { FaEdit, FaEye, FaPlus, FaTrashAlt } from "react-icons/fa"
import { Link } from "react-router-dom"
import { Col, Row } from "react-bootstrap"
import { deleteMovieComingSoon, getUpcomingMovies, allUpcomingMovies } from "../utils/AppFunction"
import MovieComingSoonPaginator from "../common/MovieComingSoonPaginator"

const ExistingMoviesComingSoon = () => {

    const [movies, setMovies] = useState([{id: "", name: "", duration: ""}])
    //const [movies, setMovies] = useState([""]);   
	const [currentPage, setCurrentPage] = useState(1)
	const [moviesPerPage] = useState(8)
	const [isLoading, setIsLoading] = useState(false)
	const [errorMessage, setErrorMessage] = useState("")
	const [successMessage, setSuccessMessage] = useState("")

    useEffect(() => {
        fetchMovies();
    },[]);

    const fetchMovies = async() => {
        setIsLoading(true);
        try{
            const result = await allUpcomingMovies();
            setMovies(result)
            setIsLoading(false);
        }
        catch(error){
            setErrorMessage(error.message)
			setIsLoading(false)
        }
    };

    useEffect(()=>{
        setCurrentPage(1);
    },[movies]);

    const handlePaginationClick = (pageNumber) => {
		setCurrentPage(pageNumber)
	}

    const handleDelete = async(movieComingSoonId) => {
        try{
            const result = await deleteMovieComingSoon(movieComingSoonId);
            if(result === ""){
                setSuccessMessage(`Movie No ${movieComingSoonId} was deleted.`)
                fetchMovies();
            }
            else{
                console.error(`Error deleting movie:  ${result.message}`);
            }
        }
        catch (error) {
			setErrorMessage(error.message)
		}
		setTimeout(() => {
			setSuccessMessage("")
			setErrorMessage("")
		}, 3000)
    };

    const calculateTotalPages = () => {
        const totalMovies = movies.length;
        return Math.ceil(totalMovies / moviesPerPage);
    };

    const indexOfLastMovie = currentPage * moviesPerPage;
    const indexOfFirstMovie = indexOfLastMovie - moviesPerPage;
    const currentMovies = movies.slice(indexOfFirstMovie, indexOfLastMovie);

    return(
        <>
        <div className="container col-md-8 col-lg-6">
				{successMessage && <p className="alert alert-success mt-5">{successMessage}</p>}

				{errorMessage && <p className="alert alert-danger mt-5">{errorMessage}</p>}
		</div>
        {isLoading ? (
            <p>Loading upcoming movies....</p>
        ) : (
            <>
            <section className="mt-5 mb-5 container">
                <div className="d-flex justify-content-between mb-3 mt-5">
                    <h2>Upcoming movies</h2>
                </div>
                <Row>
                    <Col md={6} className="d-flex justify-content-end">
						<Link to={"/add-upcoming-movie"}>
							<FaPlus /> Add Movie
						</Link>
					</Col>
                </Row>
                <table className="table table-bordered table-hover">
                    <thead>
                        <tr className="text-center">
                            <th>ID</th>
                            <th>Name</th>
                            <th>Duration</th>
                            
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {currentMovies.map((movie) => (
                            <tr key={movie.id} className="text-center">
                                <td>{movie.id}</td>
                                <td>{movie.name}</td>
                                <td>{movie.duration} min</td>
                                
                                
                                <td className="gap-2">
                                    <Link to={`/edit-upcoming-movie/${movie.id}`} className="gap-2">
										<span className="btn btn-info btn-sm">
											<FaEye />
										</span>
										<span className="btn btn-warning btn-sm ml-5">
											<FaEdit />
										</span>
									</Link>
									<button
										className="btn btn-danger btn-sm ml-5"
										onClick={() => handleDelete(movie.id)}>
										<FaTrashAlt />
									</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                <MovieComingSoonPaginator
                            currentPage={currentPage}
                            totalPages={calculateTotalPages()}
                            onPageChange={handlePaginationClick}
                />
            </section>
            </>
        )}
        </>
    );

};

export default ExistingMoviesComingSoon;