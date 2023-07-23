export type User = {
	id: string;
	username: string;
	email: string;
	roles: Role[];
};

export type Role = {
	id: string;
	name: string;
};

export type Request = {
	id: string;
	title: string;
	startDate: string;
	endDate: string;
	createdDate: string;
	reason: string;
	approval: Approval | null;
	user: User | null;
};

export type Approval = {
	id: string;
	approved: string;
	comments: string;
};

export type Notification = {
	id: string;
	content: string;
	date: string;
};
