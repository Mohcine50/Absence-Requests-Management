import { FiDelete } from "react-icons/fi";
import { Role, User } from "../types";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { RootState } from "../store";
import {
	useLocation,
	useNavigate,
	useNavigation,
	useRoutes,
} from "react-router-dom";

const UserCard = ({ user }: { user: User }) => {
	const token = useSelector<RootState>((state) => state.authReducer.token);

	const [role, setRole] = useState<string>("");
	const isAdmin = useSelector<RootState>(
		(state) => state.authReducer.isAdmin
	) as boolean;
	const username = useSelector<RootState>(
		(state) => state.authReducer.username
	) as string;

	const [showSubmitAddRole, setShowSubmitAddRole] = useState<boolean>(false);

	const navigate = useNavigate();

	useEffect(() => {
		if (role !== "") {
			setShowSubmitAddRole(true);
		}
		return () => {
			setShowSubmitAddRole(false);
		};
	}, [role]);

	const addRoleToUser = async (username: string) => {
		const response = await fetch(
			"http://localhost:8080/api/users/add-role-to-user",
			{
				method: "POST",
				headers: {
					Authorization: `Bearer ${token}`,
					"Content-Type": "application/json",
				},
				body: JSON.stringify({
					username,
					role,
				}),
			}
		);
		if (response.status < 400) {
			const data = await response.json;
			console.log(data);
			// refresh
			navigate(0);
		}
	};

	const deleteRoleFromUser = async (username: string, role: string) => {
		const response = await fetch(
			"http://localhost:8080/api/users/delete-role-from-user",
			{
				method: "DELETE",
				headers: {
					Authorization: `Bearer ${token}`,
					"Content-Type": "application/json",
				},
				body: JSON.stringify({
					username,
					role,
				}),
			}
		);
		if (response.status < 400) {
			const data = await response.json;
			console.log(data);
			// refresh
			navigate(0);
		}
	};
	return (
		<>
			<div className="flex flex-col p-4 border" key={user.id}>
				<h4>{user.username}</h4>
				<h4>{user.email}</h4>
				<>
					{user.roles.map((role: Role) => {
						return (
							<div className="flex gap-2">
								<span className="basis-full" key={role.id}>
									{role.name}
								</span>
								{user.username !== username && (
									<button
										className="p-1 "
										onClick={() => {
											deleteRoleFromUser(
												user.username,
												role.name
											);
										}}
									>
										<FiDelete />
									</button>
								)}
							</div>
						);
					})}
				</>

				<div className="mt-auto">
					<hr className="mt-3" />
					<label
						htmlFor="role"
						className="block mb-2 text-sm font-semibold text-white"
					>
						Add Role
					</label>
					<select
						value={role}
						onChange={(e) => setRole(e.currentTarget.value)}
						id="role"
						className="block w-full p-2 mb-6 text-sm text-white placeholder-gray-400 bg-gray-700 border border-gray-600 rounded-lg focus:ring-blue-500 focus:border-blue-500"
					>
						<option value="">Choose a Role</option>
						<option value="MANAGER">Manager</option>
						<option value="ADMIN">Admin</option>
					</select>
					{showSubmitAddRole && (
						<button
							onClick={() => {
								addRoleToUser(user.username);
							}}
						>
							Add Role
						</button>
					)}
				</div>
			</div>
		</>
	);
};

export default UserCard;
