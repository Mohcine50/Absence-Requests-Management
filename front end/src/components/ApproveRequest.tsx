import { useState } from "react";
import { BiSolidSend } from "react-icons/bi";
import { ImCancelCircle } from "react-icons/im";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "../store";
import { approveRequest } from "../features/requestsSlice";
import { Request } from "../types";

const ApproveRequest = ({ request }: { request: Request }) => {
	const [comment, setComment] = useState<string>("");
	const [showComment, setShowComment] = useState<boolean>(false);
	const [approveStatus, setApproveStatus] = useState<boolean>();

	const token = useSelector<RootState>(
		(state) => state.authReducer.token
	) as string;

	const dispatch = useDispatch<AppDispatch>();

	const handleApprove = (e: React.MouseEvent<HTMLButtonElement>) => {
		if (e.currentTarget.textContent === "Decline") {
			setApproveStatus(false);
		} else {
			setApproveStatus(true);
		}
		setShowComment(true);
	};

	const handleApproveSubmit = () => {
		const approved = approveStatus as boolean;
		const id = request.id;
		dispatch(approveRequest({ token, approved, comments: comment, id }));
		setComment("");
		setShowComment(false);
	};

	return (
		<div className="flex flex-col gap-2">
			<div className="flex gap-1 py-2">
				<button onClick={handleApprove} className="bg-red-600">
					Decline
				</button>
				<button onClick={handleApprove} className="bg-green-600">
					Approve
				</button>
			</div>
			{showComment && (
				<div className="flex flex-col gap-2 max-w-[100%]">
					<input
						className="p-3 rounded-md"
						value={comment}
						type="text"
						id="comment"
						placeholder="Comment"
						onChange={(e) => {
							setComment(e.currentTarget.value);
						}}
					/>
					<div className=" flex gap-2">
						<button className="w-fit" onClick={handleApproveSubmit}>
							<BiSolidSend />
						</button>
						<button
							className="w-fit"
							onClick={() => setShowComment(false)}
						>
							<ImCancelCircle />
						</button>
					</div>
				</div>
			)}
		</div>
	);
};

export default ApproveRequest;
