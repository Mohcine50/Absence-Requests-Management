import { NavLink, useLocation } from "react-router-dom";
import { AppDispatch, RootState } from "../store";
import { reset } from "../features/authSlice";
import { useDispatch, useSelector } from "react-redux";
import classNames from "classnames";
import profile from "../assets/profile-user.png";
import { IoMdNotifications } from "react-icons/io";
import { Notification } from "../types";
import {
	deleteAllNotification,
	deleteManagerAllNotifications,
	fetchAllNotifications,
} from "../features/notificationSlice";
import { useEffect, useRef, useState } from "react";
import moment from "moment";

const NavBar = () => {
	const [showNotification, setShowNotification] = useState<boolean>(false);

	const dispatch = useDispatch<AppDispatch>();
	const isAuthenticated = useSelector<RootState>(
		(state) => state.authReducer.authenticated
	) as boolean;

	const isAdmin = useSelector<RootState>(
		(state) => state.authReducer.isAdmin
	) as boolean;
	const username = useSelector<RootState>(
		(state) => state.authReducer.username
	) as string;
	const role = useSelector<RootState>(
		(state) => state.authReducer.role
	) as string;

	const token = useSelector<RootState>(
		(state) => state.authReducer.token
	) as string;

	const notifications = useSelector<RootState>(
		(state) => state.notificationReducer.notifications
	) as Notification[];

	const { pathname } = useLocation();

	const logout = () => {
		dispatch(reset());
		localStorage.clear();
	};

	const removeAll = () => {
		if (isAdmin || role === "MANAGER") {
			dispatch(deleteManagerAllNotifications({ token }));
		} else {
			dispatch(deleteAllNotification({ token, username }));
		}
	};

	return (
		<>
			<nav className="flex gap-5 pt-2 items-middle">
				{!isAuthenticated && (
					<>
						<NavLink
							className={classNames(
								" font-bold",
								pathname.includes("login")
									? "text-indigo-700 "
									: "text-gray-100"
							)}
							to="/login"
						>
							Login
						</NavLink>

						<NavLink
							className={classNames(
								" font-bold",
								pathname.includes("register")
									? "text-indigo-700 "
									: "text-gray-100"
							)}
							to="/register"
						>
							Register
						</NavLink>
					</>
				)}
				<NavLink
					to="/"
					className={classNames(
						" font-bold",
						pathname === "/" ? "text-indigo-700 " : "text-gray-100"
					)}
				>
					Dashboard
				</NavLink>
				{isAuthenticated && (
					<NavLink
						to="/absences"
						className={classNames(
							" font-bold",
							pathname.includes("absences")
								? "text-indigo-700 "
								: "text-gray-100"
						)}
					>
						{isAdmin || role === "MANAGER"
							? "All Requests"
							: "My Requests"}
					</NavLink>
				)}

				{isAuthenticated && isAdmin && (
					<NavLink
						to="/users"
						className={classNames(
							" font-bold",
							pathname.includes("users")
								? "text-indigo-700 "
								: "text-gray-100"
						)}
					>
						Manage Users
					</NavLink>
				)}
				{isAuthenticated && (
					<div className="flex items-center gap-5 ml-auto">
						<div
							className="relative"
							onClick={() => {
								setShowNotification(!showNotification);
							}}
						>
							<IoMdNotifications className="text-3xl cursor-pointer" />
							<span className="absolute bottom-[-7px] right-[-7px]  flex items-center justify-center w-5 h-5 bg-red-600 rounded-full">
								{notifications.length}
							</span>
							{showNotification && (
								<div
									id="notifications_pannel"
									className="z-20 p-3 flex flex-col gap-1 absolute top-[45px] left-[-110px] bg-gray-700 border rounded-lg w-80"
								>
									{notifications.map(
										(notification: Notification) => {
											return (
												<div
													key={notification.id}
													className="flex flex-col"
												>
													<span className="text-sm text-left">
														{notification.content}
													</span>
													<span className="text-sm text-right">
														{moment(
															notification.date.replace(
																"WEST",
																""
															)
														).fromNow()}
													</span>
												</div>
											);
										}
									)}
									{notifications.length > 0 ? (
										<span
											onClick={removeAll}
											className="text-red-600 border-t cursor-pointer"
										>
											Remove All Notification
										</span>
									) : (
										<span>
											You don't have any Notifications
										</span>
									)}
								</div>
							)}
						</div>
						<div className="relative">
							<img
								className="border-2 border-blue-500 rounded-full peer"
								src={profile}
								alt="progile image"
								width={50}
							/>
							<div className="absolute hidden p-2 bg-gray-700 border rounded-lg peer-hover:block">
								<h3>{username}</h3>
								<h3>{role}</h3>
							</div>
						</div>
						<button onClick={logout}>Logout</button>
					</div>
				)}
			</nav>
		</>
	);
};

export default NavBar;
