package com.shop.pbl6_shop_fashion.service;


import com.shop.pbl6_shop_fashion.dao.*;
import com.shop.pbl6_shop_fashion.dto.PaginationResponse;
import com.shop.pbl6_shop_fashion.dto.Product.ProductDetailDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductPromotionDto;
import com.shop.pbl6_shop_fashion.dto.order.OrderItemDto;
import com.shop.pbl6_shop_fashion.dto.ProductDetail;
import com.shop.pbl6_shop_fashion.entity.*;
import com.shop.pbl6_shop_fashion.enums.DiscountType;
import com.shop.pbl6_shop_fashion.enums.SizeType;
import com.shop.pbl6_shop_fashion.exception.ProductException;
import com.shop.pbl6_shop_fashion.util.ConnectionProvider;
import com.shop.pbl6_shop_fashion.util.ImgBBUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    SizeRepository sizeRepository;

    @Autowired
    private ProductDao productDao;

    @Autowired
    EntityManager entityManager;

    private final ProductSizeService productSizeService;

    private final SizeService sizeService;

    public ProductDetailDto getProductDetailById(Integer id) {
        return productRepository.getProductDetailById(id);
    }

    public ProductPromotionDto getProductPromotionById(Integer id) {
        return productRepository.getProductPromotionById(id);
    }

    public List<ProductDto> getNewestProduct(Integer number) {
        return productRepository.getNewestProduct(number);
    }

    public List<ProductDto> getProductByCategoryAndPage(Integer categoryId, Integer page, Integer limit) {

        return productRepository.getProductByCategoryAndPage(categoryId, page, limit);
    }

    public com.shop.pbl6_shop_fashion.dto.ProductDetailMobileDto searchProductDetail(Integer id) {
        com.shop.pbl6_shop_fashion.dto.ProductDetailMobileDto product = productDao.searchDetailProducts(id);
        System.out.println("product service: " + product);
        return product;
    }

    @Cacheable("products")

    public List<ProductDetail> getAllProducts(int page, int pageSize) {

        return productDao.getAllProducts(page,pageSize);
    }
    public PaginationResponse<ProductDetail> getProductsByCategoryorBrand(Integer category_id, Integer brand_id, int page, int pageSize){
        return productDao.getProductsByCategoryorBrand(category_id,brand_id,page,pageSize);
    @CacheEvict("products")
    public void updateImages(Integer id, List<MultipartFile> files) {
        List<String> imageUrls = new ArrayList<>();
        files.forEach(file -> {
            String imageurl = ImgBBUtils.uploadImage(file);
            imageUrls.add(imageurl);
        });
        productRepository.updateProductImages(id, imageUrls);
    }

    @CacheEvict("products")
    public void addProduct(String name, String desc, Integer price, String unit, Integer brandId, Integer
            categoryId, List<String> productSizes, List<MultipartFile> images, Integer promotionId) {

        System.out.println("get brand cate : " + LocalDateTime.now());
        Product product = new Product();
        Brand brand = entityManager.find(Brand.class, brandId);
        Category category = entityManager.find(Category.class, categoryId);

        System.out.println("Xu ly du lieu: " + LocalDateTime.now());

        Integer quantity = 0;
        List<ProductSize> sizes = convertProductSizes(productSizes, product);
        for (ProductSize productSize : sizes) {
            quantity += productSize.getQuantity();
        }

        System.out.println("get promo : " + LocalDateTime.now());

        Promotion promotion = null;
        if (promotionId != null) promotion = entityManager.find(Promotion.class, promotionId);

        product.setName(name);
        product.setDescription(desc);
        product.setStatus(null);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setQuantitySold(0);
        product.setBrand(brand);
        product.setCategory(category);
        product.setProductSizes(sizes);
        product.setUnit(unit);
        product.setComments(null);
        product.setPromotion(promotion);
        product.setCreateAt(LocalDateTime.now());
        product.setUpdateAt(null);


        Product savedProduct = productRepository.save(product);


        Session session = ConnectionProvider.openSession();
        String sql = "insert into product_images (product_id, url)\n" +
                "values ";
        List<String> imageUrls = ImgBBUtils.uploadImages(images);
        if (imageUrls.size() > 0) {
            for (String imageUrl : imageUrls) {
                sql = sql + "(" + product.getId() + ", \"" + imageUrl + "\"),";
            }
            sql = sql.substring(0, sql.length() - 1);
        }
        Transaction transaction = session.beginTransaction();
        session.createNativeQuery(sql).executeUpdate();
        transaction.commit();

        sql = "insert into product_size(product_id, quantity, quantity_sold, size_id) values ";
        for (ProductSize size : sizes) {
            sql += "(" + product.getId() + ", " + size.getQuantity() + ", 0, " + size.getSize().getId() + "),";
        }
        sql = sql.substring(0, sql.length() - 1);
        transaction.begin();
        session.createNativeQuery(sql).executeUpdate();
        transaction.commit();
        session.close();
        return;
    }

    @CacheEvict("products")
    public void updateProduct(Integer productId, String name, String desc, Integer price, String unit, Integer
            brandId,
                              Integer categoryId, List<String> productSizes, List<MultipartFile> images, Integer promotionId) {
        Product product = entityManager.find(Product.class, productId);
        if (product == null) return;
        if (name != null) product.setName(name);
        if (desc != null) product.setDescription(desc);
        if (price != null) product.setPrice(price);
        if (unit != null) product.setUnit(unit);
        if (brandId != null) product.setBrand(entityManager.find(Brand.class, brandId));
        if (categoryId != null) product.setCategory(entityManager.find(Category.class, categoryId));
        if (productSizes != null) {
            List<ProductSize> currentSizes = product.getProductSizes();
            List<ProductSize> sizeList = convertProductSizes(productSizes, product);
            for (ProductSize currentSize : currentSizes) {
                for (ProductSize size : sizeList) {
                    if (currentSize.getSize() == size.getSize()) {
                        currentSize.setQuantity(size.getQuantity());
                        sizeList.remove(size);
                        break;
                    }
                }
            }
            product.setProductSizes(sizeList);

            Integer quantity = 0;
            for (ProductSize productSize : sizeList) {
                quantity += productSize.getQuantity();
            }
            product.setQuantity(quantity);
        }
        if (promotionId != null) product.setPromotion(entityManager.find(Promotion.class, promotionId));

        product.setUpdateAt(LocalDateTime.now());

        List<String> imageUrls = ImgBBUtils.uploadImages(images);
        List<ProductImage> productImages = new ArrayList<>();
        imageUrls.forEach(imageUrl -> {
            productImages.add(ProductImage.builder().url(imageUrl).product(product).build());
        });
        product.setImages(productImages);

        Product savedProduct = productRepository.save(product);
        return;
    }

    List<ProductSize> convertProductSizes(List<String> productSizes, Product product) {
        List<ProductSize> sizes = new ArrayList<>();
        for (String str : productSizes) {
            SizeType sizeType = SizeType.valueOf(str.split(":")[0].trim());
            Integer quant = Integer.parseInt(str.split(":")[1]);
            ProductSize productSize = ProductSize.builder()
                    .size(sizeRepository.findByName(sizeType))
                    .quantity(quant)
                    .quantitySold(0)
                    .product(product)
                    .build();
            sizes.add(productSize);
        }
        return sizes;
    }
    public PaginationResponse<ProductDetail> searchProductsMobile(String keyword, Integer minprice, Integer maxprice, String category, int page, int pageSize) {
        return  productDao.searchProductsMobile(keyword, minprice, maxprice, category,page,pageSize);
    }
    public List<ProductDetail> getBestSellingProducts(Integer limit) {
        return productDao.getBestSellingProducts(limit);
    }
    public Product findById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Product not found"));
    }

    public Double getPromotionAmount(Product product) {
        Promotion promotion = product.getPromotion();
        if (promotion == null)
            return 0d;
        DiscountType discountType = promotion.getDiscountType();
        switch (discountType) {
            case AMOUNT -> {
                return promotion.getDiscountValue();
            }
            case PERCENTAGE -> {
                return product.getPrice() * promotion.getDiscountValue() / 100;
            }
        }
        return 0d;
    }

    public PaginationResponse<ProductDetail> searchProductsMobile(String keyword, Integer minprice, Integer maxprice, String category, int page, int pageSize) {
        return productDao.searchProductsMobile(keyword, minprice, maxprice, category, page, pageSize);
    }

    public List<ProductDetail> getBestSellingProducts(Integer limit) {
        return productDao.getBestSellingProducts(limit);
    }

    public List<OrderItem> calculateOrderItemAndProcessProduct(List<OrderItemDto> orderItemDtos) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItemDtos) {
            int productId = orderItemDto.getProductId();

            Product product = findById(productId);
            orderItemDto.setUnitPrice(product.getPrice() - getPromotionAmount(product));
            Size size = sizeService.findByName(orderItemDto.getSizeType());

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .size(size.getName())
                    .quantity(orderItemDto.getQuantity())
                    .unitPrice(orderItemDto.getUnitPrice())
                    .build();
            orderItems.add(orderItem);
            productSizeService.increaseSoldOut(product, size, orderItemDto.getQuantity());
        }
        return orderItems;
    }

    public List<OrderItem> calculateOrderItem(List<OrderItemDto> orderItemDtos) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItemDtos) {
            Integer productId = orderItemDto.getProductId();

            Product product = findById(productId);
            orderItemDto.setUnitPrice(orderItemDto.getUnitPrice() - getPromotionAmount(product));
            Size size = sizeService.findByName(orderItemDto.getSizeType());

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .size(size.getName())
                    .quantity(orderItemDto.getQuantity())
                    .unitPrice(orderItemDto.getUnitPrice())
                    .build();
            orderItems.add(orderItem);
        }
        return orderItems;
    }
}

