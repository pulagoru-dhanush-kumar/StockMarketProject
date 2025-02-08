<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Buy Product</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>

<h1>Buy Product</h1>

<!-- Product details section -->
<div class="product-details">
    <h2>Product: ${product.name}</h2>
    <p>Description: ${product.description}</p>
    <p>Price: $${product.price}</p>
</div>

<!-- Purchase form -->
<form action="processPurchase" method="POST">
    <input type="hidden" name="productId" value="${product.id}">
    <input type="hidden" name="userId" value="${sessionScope.userId}"> <!-- Assuming userId is stored in session -->
    <input type="number" name="quantity" placeholder="Enter quantity" min="1" required>
    <input type="hidden" name="price" value="${product.price}">
    <input type="submit" value="Buy Now">
</form>

</body>
</html>
