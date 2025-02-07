CREATE TABLE member (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id VARCHAR(50) NOT NULL COMMENT '사용자 아이디',
    user_pw VARCHAR(255) NOT NULL COMMENT '사용자 비밀번호',
    user_name VARCHAR(10) NOT NULL COMMENT '사용자 이름',
    user_telno VARCHAR(20) DEFAULT NULL COMMENT '사용자 전화번호',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정일시',
    PRIMARY KEY (id),
    UNIQUE (user_id)
);

CREATE TABLE product (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id VARCHAR(50) NOT NULL COMMENT '등록자 아이디',
    product_name VARCHAR(100) NOT NULL COMMENT '상품 이름',
    product_description VARCHAR(255) DEFAULT NULL COMMENT '상품 설명',
    price DECIMAL(10, 2) NOT NULL COMMENT '상품 가격',
    sales_status int not null default 1 comment '판매 상태',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정일시',
    PRIMARY KEY (id)
);

CREATE TABLE chat_room (
    chat_room_id CHAR(36) PRIMARY KEY COMMENT '채팅방 아이디(UUID)',
    product_id BIGINT NOT NULL COMMENT '상품 아이디',
    buyer_id VARCHAR(50) NOT NULL COMMENT '구매자 아이디',
    seller_id VARCHAR(50) NOT NULL COMMENT '판매자 아이디',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
);

CREATE TABLE chat (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '채팅 ID',
    chat_room_id CHAR(36) NOT NULL COMMENT '채팅방 아이디',
    product_id BIGINT NOT NULL COMMENT '상품 아이디',
    send_user_id VARCHAR(50) NOT NULL COMMENT '발신자 아이디',
    receive_user_id VARCHAR(50) NOT NULL COMMENT '수신자 아이디',
    message VARCHAR(500) NOT NULL COMMENT '채팅 메세지',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    CONSTRAINT fk_chat_chatroom FOREIGN KEY (chat_room_id) REFERENCES chat_room (chat_room_id) ON DELETE CASCADE
);
