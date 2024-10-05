//first request to server to create order

const paymentStart = () => {
	console.log("payment Started");

	let amount = $("#payment_field").val();
	console.log(amount)
	
	let paymentId = $("#ticketId").val();
	console.log(paymentId)

	if (amount == "" || amount == null) {
		alert("amount is required !!");
		return;
	}

	// code we will use ajax to create order 

	$.ajax(
		{
			url: '/ecommerence/users/create_order',
			data: JSON.stringify({ amount: amount, info: "order_request", ticketId: ticketId }),
			contentType: 'application/json',
			type: 'POST',
			dataType: 'json',
			success: function(response) {
				console.log(response)
				if (response.status == 'created') {
					// open payment form
					let options = {
						key: 'rzp_test_CN2otsWww3Ooa2',
						amount: response.amount,
						currency: 'INR',
						name: 'Electronic Store',
						description: 'Enjoy Services',
						image: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTBea8gWo1fQtFNXOLe1_LQ8eNhA94AEf-R1w&s',
						order_id: response.id,
						
						handler: function(response) {
							console.log(response.razorpay_payment_id)
							console.log(response.razorpay_order_id)
							console.log(response.razorpay_signature)
							console.log('Payment Successfully done ')


							updatePaymentonServer(response.razorpay_payment_id, response.razorpay_order_id, 'paid' , ticketId);

							alert('Congrats, Payment is Successfull !!')
						},
						prefill: {
							name: "",
							email: "",
							contact: ""
						},
						notes: {
							"address": "E - Store",
						},
						theme: {
							color: "#3399cc",
						},
					};

					let rzp = new Razorpay(options);

					rzp.on('payment.failed', function(response) {
						alert(response.error.code);
						alert(response.error.description);
						alert(response.error.source);
						alert(response.error.step);
						alert(response.error.reason);
						alert(response.error.metadata.order_id);
						alert(response.error.metadata.payment_id);
						alert(' Oops Payment failed !!');
					});

					rzp.open();

				}
			},
			error: function(error) {
				console.log(error)
				alert("Something went wrong")
			},

		});
};

function updatePaymentonServer(payment_id, order_id, status , ticketId) {
	$.ajax(
		{
			url: '/ecommerence/users/update_order',
			data: JSON.stringify({ payment_id: payment_id, order_id:order_id , status:status, ticketId:ticketId }),
			contentType: 'application/json',
			type: 'POST',
			dataType: 'json',
			success:function(response){
				
				window.location.href = "Success";
			},
			error:function(error){
				alert("Your payment is successfull , but Something went wrong")
			}
		});
};

function autocompleteSearch() {
    const query = document.getElementById('search-input').value;

    if (query.length > 0) {
		
		let url = `/ecommerence/users/search-product?query=${query}`;
		
        fetch(url)
        .then(response => response.json())
        .then(data => {
			console.log(data)
			
			let text = `<div>`
			
			data.forEach( (products)=>{
				text+=`<div class="search-item">
				<a href="view-page?product_id=${products.product_id}">${products.product_name}</a>
				</div>`;
				console.log(products.product_id);
			} );
			
			
			text+=`</div>`;
			document.getElementById('search-result').innerHTML=text;
        });
    } else {
        document.getElementById('search-result').hide();
    }
}