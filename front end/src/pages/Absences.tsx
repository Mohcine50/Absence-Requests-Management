import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "../store";
import { Request } from "../types";
import classNames from "classnames";
import { useEffect, useState } from "react";
import RequestModel from "../components/RequestFormModel";
import AbsenceRequest from "../components/AbsenceRequest";
import {
	fetchAllRequests,
	fetchAllRequestsByUser,
} from "../features/requestsSlice";

const Absences = () => {
	const requests = useSelector<RootState>(
		(state) => state.requestsReduer.requests
	) as Request[];

	const isAdmin = useSelector<RootState>(
		(state) => state.authReducer.isAdmin
	) as boolean;

	const role = useSelector<RootState>(
		(state) => state.authReducer.role
	) as string;

	const token = useSelector<RootState>(
		(state) => state.authReducer.token
	) as string;
	const username = useSelector<RootState>(
		(state) => state.authReducer.username
	) as string;

	const [showRequestModel, setShowRequestModel] = useState<boolean>(false);

	const dispatch = useDispatch<AppDispatch>();
	useEffect(() => {
		if (isAdmin || role === "MANAGER") {
			dispatch(fetchAllRequests({ token }));
		} else {
			dispatch(fetchAllRequestsByUser({ token, username }));
		}
	}, []);

	return (
		<div className="my-7">
			<div className="flex items-center justify-between mb-7">
				<h1 className="text-left">
					{isAdmin ? "All absences requests" : "My Abcenses Requests"}
				</h1>
				{!isAdmin && role === "USER" && (
					<button onClick={() => setShowRequestModel(true)}>
						Add New Request
					</button>
				)}
			</div>
			{showRequestModel && (
				<div className="absolute z-10 transform -translate-x-1/2 -translate-y-1/2 top-1/2 left-1/2">
					<div className="absolute top-1 right-2">
						<button
							onClick={() => {
								setShowRequestModel(false);
							}}
							className="p-2 border rounded-full"
						>
							X
						</button>
					</div>
					<RequestModel
						action="ADD"
						setShowRequestModel={setShowRequestModel}
					/>
				</div>
			)}

			<div className="flex flex-wrap gap-5">
				{requests.map((request: Request) => {
					return <AbsenceRequest request={request} />;
				})}
			</div>
		</div>
	);
};

export default Absences;
