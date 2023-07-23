import { SyntheticEvent } from "react";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

function Register() {
	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("");
	const [email, setEmail] = useState("");
	const [registerError, setRegisterError] = useState("");
	const nav = useNavigate();

	const handleSubmit = async (e: SyntheticEvent<HTMLFormElement>) => {
		e.preventDefault();
		const response = await fetch(
			"http://localhost:8080/api/auth/register",
			{
				method: "POST",
				headers: {
					"Content-Type": "application/json",
				},
				credentials: "include",
				body: JSON.stringify({
					username,
					email,
					password,
				}),
			}
		);

		if (response.status === 200) {
			setPassword("");
			setUsername("");
			setEmail("");
			nav("/login");
		} else {
			setRegisterError("Register not completed try again");
		}
	};

	return (
		<div className="flex flex-col items-center justify-center h-screen gap-5">
			<h1>Register</h1>
			<form
				className="flex w-96 flex-col gap-5 justify-center"
				onSubmit={handleSubmit}
			>
				<input
					className="p-3 rounded-md"
					type="username"
					name="username"
					id="username"
					placeholder="Username"
					value={username}
					onChange={(e) => setUsername(e.currentTarget.value)}
				/>
				<input
					className="p-3 rounded-md"
					type="email"
					name="email"
					id="email"
					placeholder="Email"
					value={email}
					onChange={(e) => setEmail(e.currentTarget.value)}
				/>
				<input
					className="p-3 rounded-md"
					type="password"
					name="password"
					id="password"
					placeholder="Password"
					value={password}
					onChange={(e) => setPassword(e.currentTarget.value)}
				/>

				<button className="bg-[#646cff] rounded-md" type="submit">
					Register
				</button>
			</form>
			{registerError !== "" && <h1>{registerError}</h1>}
			<Link to="/login">Login</Link>
		</div>
	);
}

export default Register;
