import classNames from "classnames";
import { Request } from "../types";
import { useState } from "react";
import RequestModel from "./RequestFormModel";
import { FiEdit, FiDelete } from "react-icons/fi";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "../store";
import ApproveRequest from "./ApproveRequest";
import { deleteAbseceRequest } from "../features/requestsSlice";

const AbsenceRequest = ({ request }: { request: Request }) => {
	const [showRequestModel, setShowRequestModel] = useState<boolean>(false);
	const username = useSelector<RootState>(
		(state) => state.authReducer.username
	);

	const dispatch = useDispatch<AppDispatch>();

	const isAdmin = useSelector<RootState>(
		(state) => state.authReducer.isAdmin
	) as boolean;

	const role = useSelector<RootState>(
		(state) => state.authReducer.role
	) as string;
	const token = useSelector<RootState>(
		(state) => state.authReducer.token
	) as string;

	const handleDeleteRequest = (id: string) => {
		dispatch(deleteAbseceRequest({ token, id }));
	};

	const isUser = username === request.user?.username;
	return (
		<div
			className={classNames(
				"flex flex-col max-w-xs border  rounded-lg p-3 w-60 h-fit",
				request.approval?.approved === "APPROVED"
					? "border-green-500"
					: request.approval?.approved === "DECLINED"
					? "border-red-600"
					: "border-gray-50"
			)}
		>
			{isUser && (
				<div className="relative ">
					<button
						className="absolute  right-[-8px] top-[-5px] p-1"
						onClick={() => setShowRequestModel(true)}
					>
						<FiEdit />
					</button>

					<button
						className="absolute  right-[-8px] top-[30px] p-1"
						onClick={() => handleDeleteRequest(request.id)}
					>
						<FiDelete />
					</button>
				</div>
			)}

			<h3 className="text-left truncate max-w-[200px]">
				{request.title}
			</h3>
			<h3 className="text-left grid grid-cols-2">
				<span>startDate: </span>
				{request.startDate}
			</h3>
			<h3 className="grid text-left grid-cols-2">
				<span>endDate: </span>
				{request.endDate}
			</h3>
			<h3 className="relative text-left grid grid-cols-2 group">
				<span>reason: </span>
				<h4 className="text-left truncate max-w-[120px]">
					{request.reason}
				</h4>
				<h4 className="bg-gray-700 border rounded-lg absolute top-7 text-left group-hover:block hidden">
					{request.reason}
				</h4>
			</h3>
			{showRequestModel && (
				<div className="z-10 absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2">
					<div className="absolute top-1 right-2">
						<button
							onClick={() => {
								setShowRequestModel(false);
							}}
							className="rounded-full p-2 border"
						>
							X
						</button>
					</div>
					<RequestModel
						action="EDIT"
						setShowRequestModel={setShowRequestModel}
						request={request}
					/>
				</div>
			)}
			{(isAdmin || role === "MANAGER") && (
				<ApproveRequest request={request} />
			)}
		</div>
	);
};

export default AbsenceRequest;
