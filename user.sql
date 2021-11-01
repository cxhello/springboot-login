CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `nick_name` varchar(20) NOT NULL COMMENT '用户昵称',
  `email` varchar(50) NOT NULL COMMENT '用户邮箱',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user` varchar(50) NOT NULL COMMENT '创建人',
  `update_time` datetime NULL COMMENT '更新时间',
  `update_user` varchar(50) NULL COMMENT '更新人',
  `is_delete` bigint(20) NOT NULL DEFAULT 0 COMMENT '是否删除，0-未删除，其他值已删除',
  PRIMARY KEY (`id`)
) COMMENT = '用户表';