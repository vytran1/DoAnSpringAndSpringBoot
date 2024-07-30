package com.shopme.common.entity.order;

public enum OrderStatus {
    NEW{
    		
    	@Override
    	public String defaultDescription() {
    		return "Order was created by customer";
    	}
    },
    CANCELLED{
    	@Override
    	public String defaultDescription() {
    		return "Order was rejected";
    	}
    },
    PROCESSING{
    	@Override
    	public String defaultDescription() {
    		return "Order is being processed";
    	}
    },
    PACKAGED{
    	@Override
    	public String defaultDescription() {
    		return "Product was packaged";
    	}
    },
    PICKED{
    	@Override
    	public String defaultDescription() {
    		return "Shipper pick Order";
    	}
    },
    RETURN_REQUESTED{
		@Override
		public String defaultDescription() {
			// TODO Auto-generated method stub
			return "Customer sent request to return purchase";
		} 
    },
    SHIPPING{
    	@Override
    	public String defaultDescription() {
    		return "Shipper is delivering the product";
    	}
    },
    DELIVERED{
    	@Override
    	public String defaultDescription() {
    		return "Customer received product";
    	}
    },
    RETURNED{
    	@Override
    	public String defaultDescription() {
    		return "Product was returned";
    	}
    },
    PAID{
    	@Override
    	public String defaultDescription() {
    		return "Customer has paid for product";
    	}
    },
    REFUNDED{
    	@Override
    	public String defaultDescription() {
    		return "Customer has been refunded";
    	}
    };
    public abstract String defaultDescription();
}
