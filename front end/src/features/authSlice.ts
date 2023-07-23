import { JWTPayload, decodeJwt } from "jose";
import { AnyAction, createSlice } from "@reduxjs/toolkit";
import { RootStore } from "../store";

type TInitialState = {
	token: string;
	authenticated: boolean;
	isAdmin: boolean;
	username: string;
	role: string;
};
const initialState: TInitialState = {
	token: "",
	authenticated: false,
	isAdmin: false,
	username: "",
	role: "USER",
};

export const authSlice = createSlice({
	name: "auth",
	initialState,
	reducers: {
		pushToken: (state, action) => {
			const { token } = action.payload;
			state.token = token;
			state.authenticated = true;
		},
		updateAuth: (state, action) => {
			state.authenticated = action.payload;
		},
		isAdmin: (state, action) => {
			const { admin, role } = action.payload;
			state.isAdmin = admin;
			state.role = role;
		},
		setUsername: (state, action) => {
			state.username = action.payload;
		},
		reset: (state) => {
			return initialState;
		},
	},
});

export const tokenUpdatedMiddleware =
	(store: RootStore) => (next: any) => (action: AnyAction) => {
		if (action.type === "auth/pushToken") {
			const token = action.payload.token;
			localStorage.setItem("token", token);
			localStorage.setItem("authenticated", "true");
			const result = next(action);

			const decodedJwt: JWTPayload = decodeJwt(token);
			if (isExpired(decodedJwt.exp)) store.dispatch(updateAuth(false));
			const scopes: string = decodedJwt.scope as string;
			if (scopes.includes("ADMIN")) {
				store.dispatch(isAdmin({ admin: true, role: "ADMIN" }));
			} else if (scopes.includes("MANAGER")) {
				store.dispatch(isAdmin({ admin: false, role: "MANAGER" }));
			}
			const username = decodedJwt.sub as string;
			store.dispatch(setUsername(username));
			return result;
		}
		return next(action);
	};

const isExpired = (timestamp: number | undefined) => {
	const currentTimestamp = Math.floor(Date.now() / 1000);
	return (timestamp as number) <= currentTimestamp;
};

export const { pushToken, updateAuth, isAdmin, reset, setUsername } =
	authSlice.actions;

export default authSlice.reducer;
