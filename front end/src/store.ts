import { configureStore } from "@reduxjs/toolkit";
import authReducer, { tokenUpdatedMiddleware } from "./features/authSlice";
import requestsReduer from "./features/requestsSlice";
import notificationReducer from "./features/notificationSlice";

const store = configureStore({
	reducer: {
		authReducer,
		requestsReduer,
		notificationReducer,
	},
	middleware: (getDefaultMiddleware) =>
		getDefaultMiddleware().concat(tokenUpdatedMiddleware),
});

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
export type RootStore = ReturnType<typeof store>;

export default store;
