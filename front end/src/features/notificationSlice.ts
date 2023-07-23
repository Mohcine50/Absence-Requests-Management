import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { Notification } from "../types";

export const fetchManagerAllNotifications = createAsyncThunk(
	"notification/fetchMangerAllNotifications",
	async ({ token }: { token: string }) => {
		const response = await fetch(
			`http://localhost:8080/api/managerNotifications/all`,
			{
				headers: {
					Authorization: `Bearer ${token}`,
				},
			}
		);
		if (response.status < 400) {
			const data = await response.json();
			return data;
		}
	}
);

export const deleteManagerAllNotifications = createAsyncThunk(
	"notification/deleteMangerAllNotifications",
	async ({ token }: { token: string }) => {
		const response = await fetch(
			`http://localhost:8080/api/managerNotifications/deleteAll`,
			{
				method: "DELETE",
				headers: {
					Authorization: `Bearer ${token}`,
				},
			}
		);
		if (response.status < 400) {
			const data = await response.json();
			return data;
		}
	}
);

export const fetchAllNotifications = createAsyncThunk(
	"notification/fetchAllNotifications",
	async ({ token, username }: { token: string; username: string }) => {
		const response = await fetch(
			`http://localhost:8080/api/notifications/${username}/all`,
			{
				headers: {
					Authorization: `Bearer ${token}`,
				},
			}
		);
		if (response.status < 400) {
			const data = await response.json();
			return data;
		}
	}
);

export const deleteAllNotification = createAsyncThunk(
	"notification/deleteAllNotifications",
	async ({ token, username }: { token: string; username: string }) => {
		const response = await fetch(
			`http://localhost:8080/api/notifications/${username}/deleteAll`,
			{
				method: "DELETE",
				headers: {
					Authorization: `Bearer ${token}`,
				},
			}
		);
		if (response.status < 400) {
			const data = await response.json();
			return data;
		}
	}
);

type TInitaialState = {
	notifications: Notification[];
};

const initialState: TInitaialState = {
	notifications: [],
};

export const notificationSlice = createSlice({
	name: "notification",
	initialState,
	reducers: {},
	extraReducers(builder) {
		builder.addCase(fetchAllNotifications.fulfilled, (state, action) => {
			state.notifications = action.payload;
		});
		builder.addCase(deleteAllNotification.fulfilled, (state, action) => {
			state.notifications = [];
		});
		builder.addCase(
			fetchManagerAllNotifications.fulfilled,
			(state, action) => {
				state.notifications = action.payload;
			}
		);
		builder.addCase(
			deleteManagerAllNotifications.fulfilled,
			(state, action) => {
				state.notifications = [];
			}
		);
	},
});

export default notificationSlice.reducer;
export const reducers = notificationSlice.actions;
