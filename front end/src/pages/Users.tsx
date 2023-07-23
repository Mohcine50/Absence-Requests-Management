import { useSelector } from "react-redux";
import { RootState } from "../store";
import { Role, User } from "../types";
import { useEffect, useState } from "react";
import { FiDelete } from "react-icons/fi";
import UserCard from "../components/UserCard";

const Users = () => {
	const token = useSelector<RootState>((state) => state.authReducer.token);
	const [users, setUsers] = useState<User[]>([]);

	const fetchUserList = async () => {
		const response = await fetch("http://localhost:8080/api/users/all", {
			headers: {
				Authorization: `Bearer ${token}`,
			},
		});

		if (response.status === 200) {
			const data = await response.json();
			setUsers(data);
		}
	};

	useEffect(() => {
		fetchUserList().finally();
	}, []);

	return (
		<>
			<h1 className="my-6">Manage Users</h1>
			<div className="flex gap-5">
				{users.map((user: User) => {
					return <UserCard user={user} />;
				})}
			</div>
		</>
	);
};

export default Users;
