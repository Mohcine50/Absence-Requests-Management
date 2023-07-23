import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.tsx";
import "./index.css";
import store from "./store.ts";
import { Provider } from "react-redux";
import { pushToken } from "./features/authSlice.ts";
import { BrowserRouter } from "react-router-dom";

const token = localStorage.getItem("token") as string;

if (token !== null && token !== undefined && token !== "undefined")
	store.dispatch(pushToken({ token }));

ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
	<React.StrictMode>
		<BrowserRouter>
			<Provider store={store}>
				<App />
			</Provider>
		</BrowserRouter>
	</React.StrictMode>
);
