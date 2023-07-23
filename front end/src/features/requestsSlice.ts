import { Request } from "./../types";
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

export const approveRequest = createAsyncThunk(
	"requests/approveRequest",
	async ({
		token,
		approved,
		comments,
		id,
	}: {
		token: string;
		approved: boolean;
		comments: string;
		id: string;
	}) => {
		const response = await fetch(
			`http://localhost:8080/api/approvals/${id}/approve`,
			{
				method: "PUT",
				headers: {
					Authorization: `Bearer ${token}`,
					"Content-Type": "application/json",
				},
				body: JSON.stringify({ approved, comments }),
			}
		);
		if (response.status < 400) {
			const data = await response.json();
			return [data.absence, id];
		}
	}
);

export const deleteAbseceRequest = createAsyncThunk(
	"requests/deleteRequest",
	async ({ token, id }: { token: string; id: string }) => {
		const response = await fetch(
			`http://localhost:8080/api/absences/${id}/delete`,
			{
				method: "DELETE",
				headers: {
					Authorization: `Bearer ${token}`,
					"Content-Type": "application/json",
				},
			}
		);
		if (response.status < 400) {
			return [id];
		}
	}
);

export const editRequest = createAsyncThunk(
	"requests/editRequest",
	async ({
		token,
		title,
		startDate,
		endDate,
		reason,
		id,
	}: {
		title: string;
		token: string;
		startDate: string;
		endDate: string;
		reason: string;
		id: string;
	}) => {
		const response = await fetch(
			`http://localhost:8080/api/absences/${id}/edit`,
			{
				method: "PUT",
				headers: {
					Authorization: `Bearer ${token}`,
					"Content-Type": "application/json",
				},
				body: JSON.stringify({ title, startDate, endDate, reason }),
			}
		);
		if (response.status < 400) {
			const data = await response.json();
			return data;
		}
	}
);

export const addRequest = createAsyncThunk(
	"requests/addRequest",
	async ({
		title,
		token,
		startDate,
		endDate,
		reason,
	}: {
		title: string;
		token: string;
		startDate: string;
		endDate: string;
		reason: string;
	}) => {
		const response = await fetch("http://localhost:8080/api/absences/new", {
			method: "POST",
			headers: {
				Authorization: `Bearer ${token}`,
				"Content-Type": "application/json",
			},
			body: JSON.stringify({ title, startDate, endDate, reason }),
		});
		if (response.status < 400) {
			const data = await response.json();
			return data;
		}
	}
);

export const fetchAllRequests = createAsyncThunk(
	"requests/fetchRequests",
	async ({ token }: { token: string }) => {
		const response = await fetch("http://localhost:8080/api/absences/all", {
			headers: {
				Authorization: `Bearer ${token}`,
			},
		});
		if (response.status < 400) {
			const data = await response.json();
			return data;
		}
	}
);

export const fetchAllRequestsByUser = createAsyncThunk(
	"requests/fetchRequestsByUser",
	async ({ token, username }: { token: string; username: string }) => {
		const response = await fetch(
			`http://localhost:8080/api/absences/${username}/absences/all`,
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

type TInitialState = {
	requests: Request[];
};

const initialState: TInitialState = {
	requests: [],
};

export const requestsSlice = createSlice({
	name: "requests",
	initialState,
	reducers: {},
	extraReducers(builder) {
		builder.addCase(fetchAllRequests.fulfilled, (state, action) => {
			state.requests = action.payload;
		});
		builder.addCase(fetchAllRequestsByUser.fulfilled, (state, action) => {
			state.requests = action.payload;
		});
		builder.addCase(addRequest.fulfilled, (state, action) => {
			state.requests.push(action.payload.Absence);
		});
		builder.addCase(editRequest.fulfilled, (state, action) => {
			const request = action.payload;
			const updatedRequests = state.requests.map((_request) => {
				if (request.id === _request.id) {
					return request;
				}
				return _request;
			});
			state.requests = updatedRequests;
		});
		builder.addCase(approveRequest.fulfilled, (state, action) => {
			if (action.payload) {
				const [approval, id] = action.payload;
				const updatedRequests = state.requests.map(
					(_request: Request) => {
						if (id === _request.id) {
							{
								return { ..._request, approval };
							}
						}
						return _request;
					}
				);
				state.requests = updatedRequests;
			}
		});
		builder.addCase(deleteAbseceRequest.fulfilled, (state, action) => {
			if (action.payload) {
				const [id] = action.payload;
				console.log(id);
				const updatedRequests = state.requests.filter((request) => {
					return request.id !== id;
				});
				state.requests = updatedRequests;
			}
		});
	},
});

export default requestsSlice.reducer;
export const reducers = requestsSlice.actions;
