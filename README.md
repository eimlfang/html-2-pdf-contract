# 在线合同生成器

在线合同生成器通过解析html文件，替换html文件中的占位符将需要填充的内容，继而生成PDF。

## 合同内容填充参数
合同内容填充参数通过双花括号作为占位符: `{{xx}}`

合同参数格式为 `类型_名称`,例如：`p_contractNo`，意思为该填充参数为`p`文本，名称为`contractNo`。
类型标识的主要作用时给客户端渲染交互设计。

### 参数类型对照表
|标识|类型|
|:----:|:----:|
|t|文本|
|n|数字|
|p|手机号|
|d|日期|
|h|时间（小时:分钟）|
|dt|日期时间|
|b|布尔型, 字符表示，“0”：false, "1": true|
|m|金额，带小数的文本，只能有数字和小数点|

## 使用方法

```java
public static void main(String[] args) throws DocumentException, IOException {
        Map pars = JSON.parseObject(paramDemo(), Map.class);
        try (ContractGenerator generator = new DefaultContractGenerator(HTML2, "D:/temp/assets", 1, pars)) {
            File finalOutputFile = generator.generate();
            System.out.println("最终输出文件：" + finalOutputFile.getAbsolutePath());
        }
}
```