package com.shopme.review.vote;

public class VoteResult {
    private boolean successfull;
    private String message;
    private int voteCount;
    
    
    public static VoteResult fail(String message) {
    	return new VoteResult(false, message, 0);
    }
    
    public static VoteResult success(String message,int voteCount) {
    	return new VoteResult(true, message,voteCount );
    }
    
    
	private VoteResult(boolean successfull, String message, int voteCount) {
		super();
		this.successfull = successfull;
		this.message = message;
		this.voteCount = voteCount;
	}
	public boolean isSuccessfull() {
		return successfull;
	}
	public void setSuccessfull(boolean successfull) {
		this.successfull = successfull;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
    
    
    
}
