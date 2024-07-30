var productDetailCount;
$(document).ready(function(){
	productDetailCount = $(".hiddenProductId").length;
	$("#products").on("click","#linkAddProduct",function(e){
		e.preventDefault();
		
		link = $(this);
		url = link.attr("href");
		
		$("#addProductModal").on("shown.bs.modal",function(){
			$(this).find("iframe").attr("src",url);
		});
		$("#addProductModal").modal();
	})
})

function addProduct(productId,productName){
	$("#addProductModal").modal("hide");
	
    getShippingCost(productId);
}

function getShippingCost(productId){
	selectedCountry = $("#country option:selected");
	countryId = selectedCountry.val();
	
	state = $("#state").val();
	if(state.lenght == 0){
		state = $("#city").val();
	}
	//alert("state " + state +"; countryId " + countryId);
	
	requestURL = contextPath + "get_shipping_cost";
	
	params = {
		productId:productId,
		countryId:countryId,
		stateId:state
	};
	
	
	$.ajax({
		 type:"POST",
		 url: requestURL,
		 beforeSend: function(xhr){
			  xhr.setRequestHeader(csrfHeaderName,csrfValue); 
		 },
		 data: params,
	 }).done(function(shippingCost){
		 getProductInfo(productId,shippingCost);
	 }).fail(function(err){
		 showErrorModal(err.responseJSON.message);
		 shippingCost = 0.0;
		 getProductInfo(productId,shippingCost);
	 }).always(function(){
		 $("#addProductModal").modal("hide");
	 });
	 
}

function getProductInfo(productId,shippingCost){
	requestURL = contextPath + "products/get/" + productId;
	
	$.get(requestURL,function(productJSON){
		console.log(productJSON);
		productName = productJSON.name;
		mainImagePath = contextPath.substring(0,contextPath.length -1) + productJSON.imagePath;
		productCost = $.number(productJSON.cost,2);
		productPrice = $.number(productJSON.price,2);
		
		htmlCode = insertProductInfo(productId,productName,mainImagePath,productCost,productPrice,shippingCost);
		$("#productList").append(htmlCode);
		updateOrderAmounts();
	}).fail(function(err){
		showErrorModal(err.responseJSON.message);
	});
}

function insertProductInfo(productId,productName,mainImagePath,productCost,productPrice,shippingCost){
	nextCount = productDetailCount + 1;
	productDetailCount++;
	rowId = "row" +nextCount;
	quantityId = "quantity" + nextCount;
	priceId = "price" + nextCount;
	subtotalId = "subtotal" + nextCount;
	blankLineId = "blankLine" + nextCount
	htmlCode = `
	     <div class="border rounded" id="${rowId}" >
	                 <input type="hidden" name="detailId"  value="0"/> 
                     <input type="hidden" name="productId" value="${productId}" class="hiddenProductId">
                     <!-- countr and mainImage of products -->
                          <div class="row">
                               <div class="col-1">
                                     <div class="divCount">${nextCount}</div>
                               </div>
                                <div>
                                       <a href="" class="fas fa-trash fa-2x icon-dark linkRemove"
                                             rowNumber="${nextCount}"
                                       >
                                       </a>
                               </div>
                               <div class="col-3">
                                    <img alt="" src="${mainImagePath}" class="img-fluid">
                               </div>
                          </div>
                          
                       <!-- Name of product -->
                       <div class="row m-2">
                           <b>${productName}</b>
                       </div>
                       
                       <div class="row m-2">
                            <table>
                                  <tr>
                                       <td>Product cost:</td>
                                       <td>
                                           <input type="text" required="required" 
                                           name="productDetailCost"
                                           class="form-control cost-input" value="${productCost}"
                                           rowNumber="${nextCount}"
                                           style="max-width: 140px"
                                           >
                                       </td>
                                  </tr>
                                   <tr>
                                       <td>Quantity:</td>
                                       <td>
                                           <input type="number" step="1" min="1" max="5" required="required" 
                                            name="quantity"
                                           class="form-control quantity-input" value="1"
                                           rowNumber="${nextCount}"
                                           id="${quantityId}"
                                           style="max-width: 140px"
                                           >
                                       </td>
                                  </tr>
                                  <tr>
                                       <td>Unit Price:</td>
                                       <td>
                                           <input type="text"  required="required" 
                                           name="productPrice"
                                           class="form-control price-input" value="${productPrice}"
                                           id="${priceId}"
                                           rowNumber="${nextCount}"
                                           style="max-width: 140px"
                                           >
                                       </td>
                                  </tr>
                                  <tr>
                                       <td>Subtotal:</td>
                                       <td>
                                           <input type="text"  readonly="readonly" 
                                           name="productSubtotal"
                                           class="form-control subtotal-output" value="${productPrice}"
                                           id = "${subtotalId}"
                                           style="max-width: 140px"
                                           />
                                       </td>
                                  </tr>
                                   <tr>
                                       <td>Shipping Cost:</td>
                                       <td>
                                           <input type="text"  required="required" 
                                            name="productShippingCost"
                                           class="form-control ship-input" value="${shippingCost}"
                                           style="max-width: 140px"
                                           >
                                       </td>
                                  </tr>
                            </table>
                       </div>
                       
                     </div>
                    <div id="${blankLineId}" class="row">&nbsp;</div>
	`;
	return htmlCode;
}
function isProductAlreadyAdded(productId){
	productExist = false;
	$(".hiddenProductId").each(function(e){
		aProduct = $(this).val();
		if(aProduct == productId){
			productExist = true;
			return;
		}
	})
	return productExist;
}