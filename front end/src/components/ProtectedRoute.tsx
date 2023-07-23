import { useSelector } from "react-redux";
import { RootState } from "../store";
import { Navigate, useLocation } from "react-router-dom";
import { ReactNode } from "react";

const ProtectedRoute = ({ children }: { children: ReactNode }) => {
	const isAuthenticated = useSelector<RootState>(
		(state) => state.authReducer.authenticated
	) as boolean;

	const { pathname } = useLocation();

	if (pathname !== "/login" && pathname !== "/register" && !isAuthenticated) {
		return <Navigate to="/login" replace={true} />;
	}
	if (
		(pathname.match("/login") || pathname.match("/register")) &&
		isAuthenticated
	) {
		return <Navigate to="/" replace={true} />;
	}
	return <>{children}</>;
};

export default ProtectedRoute;
