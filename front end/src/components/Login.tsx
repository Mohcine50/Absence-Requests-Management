import { SyntheticEvent } from "react";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { AppDispatch } from "../store";
import { pushToken } from "../features/authSlice";

function Login() {
	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("");
	const [errorMessage, setErrorMessage] = useState("");
	const nav = useNavigate();

	const dispatch = useDispatch<AppDispatch>();

	const handleSubmit = async (e: SyntheticEvent<HTMLFormElement>) => {
		e.preventDefault();
		const response = await fetch("http://localhost:8080/api/auth/login", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},
			credentials: "include",
			body: JSON.stringify({
				username,
				password,
			}),
		});

		const data = await response.json();
		if (response.status === 200) {
			dispatch(pushToken({ token: data.accessToken }));
			setPassword("");
			setUsername("");
			nav("/dashboard");
			return;
		}
		setErrorMessage(data.Message);
	};

	return (
		<div className="flex flex-col items-center justify-center h-screen gap-5">
			<h1>Login</h1>
			<form
				className="flex w-96 flex-col gap-5 justify-center"
				onSubmit={handleSubmit}
			>
				<input
					className="p-3 rounded-md"
					type="username"
					name="username"
					id="username"
					value={username}
					onChange={(e) => setUsername(e.currentTarget.value)}
				/>
				<input
					className="p-3 rounded-md"
					type="password"
					name="password"
					id="password"
					value={password}
					onChange={(e) => setPassword(e.currentTarget.value)}
				/>
				<button className="bg-[#646cff] rounded-md" type="submit">
					Login
				</button>
			</form>

			{errorMessage !== "" && <h1>{errorMessage}</h1>}

			<Link to="/register">Register</Link>
		</div>
	);
}

export default Login;
