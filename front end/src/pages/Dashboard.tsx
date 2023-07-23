import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "../store";
import { useEffect, useState } from "react";
import { User, Request } from "../types";
import {
	fetchAllRequests,
	fetchAllRequestsByUser,
} from "../features/requestsSlice";
import {
	fetchAllNotifications,
	fetchManagerAllNotifications,
} from "../features/notificationSlice";

const Dashboard = () => {
	const token = useSelector<RootState>(
		(state) => state.authReducer.token
	) as string;
	const username = useSelector<RootState>(
		(state) => state.authReducer.username
	) as string;
	const dispatch = useDispatch<AppDispatch>();

	const requests = useSelector<RootState>(
		(state) => state.requestsReduer.requests
	) as Request[];

	const isAdmin = useSelector<RootState>(
		(state) => state.authReducer.isAdmin
	) as boolean;
	const role = useSelector<RootState>(
		(state) => state.authReducer.role
	) as string;

	const [user, setUser] = useState<User>();

	const getUserInfos = async () => {
		const response = await fetch(
			`http://localhost:8080/api/users/username/${username}`,
			{
				headers: {
					Authorization: `Bearer ${token}`,
				},
			}
		);

		if (response.status < 400) {
			const data = await response.json();
			setUser(data);
		}
	};

	useEffect(() => {
		if (isAdmin || role === "MANAGER") {
			dispatch(fetchAllRequests({ token }));
			dispatch(fetchManagerAllNotifications({ token }));
		} else {
			dispatch(fetchAllRequestsByUser({ token, username }));
			dispatch(fetchAllNotifications({ token, username }));
		}
		getUserInfos().finally();
	}, []);

	// need handle request promise using loading state on the slice
	return (
		<div className="h-full">
			<h1 className="font-semibold text-left my-7">Dashboard</h1>
			<div className="flex justify-between h-full gap-5 max-w-screen">
				<div className="flex flex-col w-full gap-5 p-4 border rounded-lg h-fit border-gray-50">
					<h2>My Infos</h2>
					<div>
						<h3 className="grid grid-cols-2 text-left ">
							<span>Username: </span>
							{user?.username}
						</h3>
						<h3 className="grid grid-cols-2 text-left">
							<span>Email: </span>
							{user?.email}
						</h3>
						<h3 className="grid grid-cols-2 text-left">
							<span>Roles: </span>
							<div className="flex gap-4">
								{user?.roles.map((role) => {
									return (
										<span key={role.id}>{role.name}</span>
									);
								})}
							</div>
						</h3>
					</div>
				</div>

				<div className="w-full p-4 border rounded-lg border-gray-50 h-fit">
					<div>
						<h3>
							{isAdmin ? "All requests" : "My Total Requests"} (
							<span>{requests.length}</span>)
						</h3>
						<div>
							{requests.map((request: Request, idx) => {
								return (
									<div className="text-start">
										<span>{idx + 1}</span> -{" "}
										<span>{request.reason}</span> (
										<span className="font-semibold text-sky-600">
											{request.user?.username}
										</span>
										)
									</div>
								);
							})}
						</div>
					</div>
					<hr />
					<div>
						<h3>Approved Requests</h3>
					</div>
				</div>
			</div>
		</div>
	);
};

export default Dashboard;
