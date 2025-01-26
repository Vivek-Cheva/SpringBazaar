import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";

const UpdateProduct = () => {
  if(localStorage.getItem("email")==null) window.location.assign('/login');
  const { id } = useParams();
  const [product, setProduct] = useState({});
  const [image, setImage] = useState(null);
  const [updateProduct, setUpdateProduct] = useState({
    id: null,
    name: "",
    description: "",
    brand: "",
    price: "",
    category: "",
    releaseDate: "",
    productAvailable: false,
    stockQuantity: "",
  });

  // Fetch product details
  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/product/${id}`
        );
        setProduct(response.data);
        setUpdateProduct(response.data);

        // Fetch the existing image
        const imageResponse = await axios.get(
          `http://localhost:8080/api/product/${id}/image`,
          { responseType: "blob" }
        );
        const imageFile = new File(
          [imageResponse.data],
          response.data.imageName,
          { type: imageResponse.data.type }
        );
        setImage(imageFile);
      } catch (error) {
        console.error("Error fetching product:", error);
      }
    };

    fetchProduct();
  }, [id]);

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("imageFile", image);
    formData.append(
      "product",
      new Blob([JSON.stringify(updateProduct)], { type: "application/json" })
    );

    try {
      const response = await axios.put(
        `http://localhost:8080/api/product/${id}`,
        formData,
        {
          headers: { "Content-Type": "multipart/form-data" },
        }
      );
      console.log("Product updated successfully:", response.data);
      alert("Product updated successfully!");
    } catch (error) {
      console.error("Error updating product:", error);
      alert("Failed to update product. Please try again.");
    }
  };

  // Handle input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setUpdateProduct({ ...updateProduct, [name]: value });
  };

  // Handle image change
  const handleImageChange = (e) => {
    setImage(e.target.files[0]);
  };

  return (
    <div className="update-product-container">
      <div className="center-container" style={{ marginTop: "7rem" }}>
        <h1>Update Product</h1>
        <form className="row g-3 pt-1" onSubmit={handleSubmit}>
          {/* Form inputs */}
          <div className="col-md-6">
            <label className="form-label">Name</label>
            <input
              type="text"
              className="form-control"
              value={updateProduct.name || ""}
              onChange={handleChange}
              name="name"
            />
          </div>
          <div className="col-md-6">
            <label className="form-label">Brand</label>
            <input
              type="text"
              className="form-control"
              value={updateProduct.brand || ""}
              onChange={handleChange}
              name="brand"
            />
          </div>
          <div className="col-12">
            <label className="form-label">Description</label>
            <input
              type="text"
              className="form-control"
              value={updateProduct.description || ""}
              onChange={handleChange}
              name="description"
            />
          </div>
          <div className="col-6">
            <label className="form-label">Price</label>
            <input
              type="number"
              className="form-control"
              value={updateProduct.price || ""}
              onChange={handleChange}
              name="price"
            />
          </div>
          <div className="col-md-6">
            <label className="form-label">Category</label>
            <select
              className="form-select"
              value={updateProduct.category || ""}
              onChange={handleChange}
              name="category"
            >
              <option value="">Select category</option>
              <option value="laptop">Laptop</option>
              <option value="headphone">Headphone</option>
              <option value="mobile">Mobile</option>
              <option value="electronics">Electronics</option>
              <option value="toys">Toys</option>
              <option value="fashion">Fashion</option>
            </select>
          </div>
          <div className="col-md-6">
            <label className="form-label">Image</label>
            <img
              src={image ? URL.createObjectURL(image) : ""}
              alt="Preview"
              style={{ width: "100%", height: "180px", objectFit: "cover" }}
            />
            <input
              type="file"
              className="form-control"
              onChange={handleImageChange}
            />
          </div>
          <div className="col-md-6">
            <label className="form-label">Stock Quantity</label>
            <input
              type="number"
              className="form-control"
              value={updateProduct.stockQuantity || ""}
              onChange={handleChange}
              name="stockQuantity"
            />
          </div>
          <div className="col-md-12">
            <button type="submit" className="btn btn-primary">
              Update Product
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default UpdateProduct;
