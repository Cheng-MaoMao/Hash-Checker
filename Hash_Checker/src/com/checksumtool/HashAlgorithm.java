package com.checksumtool;

// 定义一个枚举类，用于表示哈希算法
public enum HashAlgorithm {
    // 定义枚举常量，分别表示不同的哈希算法
    MD5("MD5"),
    SHA1("SHA-1"),
    SHA224("SHA-224"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512"),
    SHA3_224("SHA3-224"),
    SHA3_256("SHA3-256"),
    SHA3_384("SHA3-384"),
    SHA3_512("SHA3-512");

    // 定义一个私有变量，用于存储哈希算法的名称
    private final String algorithmName;

    // 构造函数，用于初始化哈希算法的名称
    HashAlgorithm(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    // 获取哈希算法的名称
    public String getAlgorithmName() {
        return algorithmName;
    }

    // 重写toString方法，返回哈希算法的名称
    @Override
    public String toString() {
        return algorithmName;
    }
}
