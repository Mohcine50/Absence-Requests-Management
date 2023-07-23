import "./App.css";
import { Outlet, Route, Routes } from "react-router-dom";
import NavBar from "./components/NavBar";
import Login from "./components/Login";
import Register from "./components/Register";
import Dashboard from "./pages/Dashboard";
import ProtectedRoute from "./components/ProtectedRoute";
import Absences from "./pages/Absences";
import Users from "./pages/Users";

function App() {
	return (
		<div className="home h-screen">
			<NavBar />
			<Routes>
				<Route>
					<Route
						path="/"
						element={
							<ProtectedRoute>
								<Dashboard />
							</ProtectedRoute>
						}
					/>
					<Route
						path="login"
						element={
							<ProtectedRoute>
								<Login />
							</ProtectedRoute>
						}
					/>
					<Route
						path="register"
						element={
							<ProtectedRoute>
								<Register />
							</ProtectedRoute>
						}
					/>
					<Route
						path="absences"
						element={
							<ProtectedRoute>
								<Absences />
							</ProtectedRoute>
						}
					/>
					<Route
						path="users"
						element={
							<ProtectedRoute>
								<Users />
							</ProtectedRoute>
						}
					/>
				</Route>
			</Routes>

			<Outlet />
		</div>
	);
}

export default App;
