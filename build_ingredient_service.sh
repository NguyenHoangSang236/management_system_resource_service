#!/bin/bash

# Xoá file JAR trước khi build
rm target/ingredient_service-0.0.1-SNAPSHOT.jar

# Build project và tạo file JAR
./mvnw clean install

# Kiểm tra xem file JAR có được tạo ra hay không
if [ ! -f target/ingredient_service-0.0.1-SNAPSHOT.jar ]; then
  echo "Build failed: JAR file not found."
  exit 1
fi

# Xóa các file chia tách cũ (nếu có)
rm -f splited-app-* || true

# Chia tách file JAR thành các file nhỏ hơn
split -b 21M target/ingredient_service-0.0.1-SNAPSHOT.jar splited-app-

# Thêm và commit các file chia tách vào git
git add .
git commit -m 'deploy'
git push origin deployment

# Kết nối SSH và thực hiện các lệnh trên máy đích
ssh root@103.200.20.153 << 'EOF'
cd management_system_ingredient_service
git pull origin deployment

# Kiểm tra xem các file chia tách có tồn tại hay không
if [ ! -f splited-app-aa ]; then
  echo "Deploy failed: Split files not found."
  exit 1
fi

# Xoá JAR file
rm target/ingredient_service-0.0.1-SNAPSHOT.jar

# Tạo thư mục target nếu chưa tồn tại
mkdir -p target

# Hợp lại các file chia tách thành file JAR
cat splited-app-* > target/ingredient_service-0.0.1-SNAPSHOT.jar

# Kiểm tra xem file JAR đã hợp lại có tồn tại hay không
if [ ! -f target/ingredient_service-0.0.1-SNAPSHOT.jar ]; then
  echo "Deploy failed: Reassembled JAR file not found."
  exit 1
fi

# Xóa container cũ, build và run container mới
docker rm -f ingredient-service-container
docker build -t ingredient-service-image .
docker run -p8082:8082 --name ingredient-service-container ingredient-service-image
EOF