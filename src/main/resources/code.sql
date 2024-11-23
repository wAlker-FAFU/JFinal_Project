SELECT o.*, u.username, p.name AS product_name
FROM `order` o
         LEFT JOIN user u ON o.user_id = u.id
         LEFT JOIN product p ON o.product_id = p.id
