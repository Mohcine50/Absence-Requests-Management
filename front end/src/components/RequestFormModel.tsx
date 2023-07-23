import { SyntheticEvent, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "../store";
import { addRequest, editRequest } from "../features/requestsSlice";
import { Request } from "../types";

type props = {
	setShowRequestModel: React.Dispatch<React.SetStateAction<boolean>>;
	request?: Request;
	action: "ADD" | "EDIT";
};
const request: Request = {
	startDate: "",
	id: "",
	endDate: "",
	reason: "",
	user: null,
	approval: null,
	title: "",
	createdDate: "",
};

const RequestFormModel = ({
	setShowRequestModel,
	request: request,
	action = "ADD",
}: props) => {
	const [startDate, setStartDate] = useState<string | undefined>(
		request?.startDate
	);
	const [endDate, setEndDate] = useState<string | undefined>(
		request?.endDate
	);
	const [reason, setReason] = useState<string | undefined>(request?.reason);
	const [title, setTitle] = useState<string | undefined>(request?.title);

	const token = useSelector<RootState>(
		(state) => state.authReducer.token
	) as string;
	const dispatch = useDispatch<AppDispatch>();

	const handelSubmit = (e: SyntheticEvent<HTMLFormElement>) => {
		e.preventDefault();
		const _startDate = startDate as string;
		const _endDate = endDate as string;
		const _reason = reason as string;
		const _title = title as string;
		const id = request?.id as string;
		if (action === "ADD") {
			dispatch(
				addRequest({
					token,
					startDate: _startDate,
					endDate: _endDate,
					reason: _reason,
					title: _title,
				})
			);
		} else if (action === "EDIT") {
			dispatch(
				editRequest({
					id,
					token,
					startDate: _startDate,
					endDate: _endDate,
					reason: _reason,
					title: _title,
				})
			);
		}

		setShowRequestModel(false);
	};

	return (
		<>
			<form
				className="bg-gray-700 flex flex-col w-[450px] gap-4 p-3 border border-gray-700 rounded-xl shadow"
				onSubmit={handelSubmit}
			>
				<div className="flex flex-col">
					<label className="p-3 text-left rounded-md" htmlFor="title">
						Title
					</label>
					<input
						className="p-3 rounded-md"
						type="text"
						id="title"
						name="title"
						value={title}
						onChange={(e) => setTitle(e.currentTarget.value)}
					/>
				</div>
				<div className="flex flex-col">
					<label
						className="p-3 text-left rounded-md"
						htmlFor="startDate"
					>
						Start Date
					</label>
					<input
						className="p-3 rounded-md"
						type="date"
						id="startDate"
						name="startDate"
						value={startDate}
						onChange={(e) => setStartDate(e.currentTarget.value)}
					/>
				</div>
				<div className="flex flex-col">
					<label
						className="p-3 text-left rounded-md"
						htmlFor="endDate"
					>
						End Date
					</label>
					<input
						className="p-3 rounded-md"
						type="date"
						id="endDate"
						value={endDate}
						name="endDate"
						onChange={(e) => setEndDate(e.currentTarget.value)}
					/>
				</div>
				<div className="flex flex-col">
					<label
						className="p-3 text-left rounded-md"
						htmlFor="reason"
					>
						Reason
					</label>
					<input
						className="p-3 rounded-md"
						type="text"
						id="reason"
						name="reason"
						value={reason}
						onChange={(e) => setReason(e.currentTarget.value)}
					/>
				</div>
				<div className="flex items-center p-6 space-x-2 border-t border-gray-200 rounded-b dark:border-gray-600">
					<button
						data-modal-hide="defaultModal"
						type="submit"
						className="text-white   focus:ring-4 focus:outline-none  font-medium rounded-lg text-sm px-5 py-2.5 text-center bg-blue-600 hover:bg-blue-700 focus:ring-blue-800"
					>
						Add Request
					</button>
				</div>
			</form>
		</>
	);
};

export default RequestFormModel;
