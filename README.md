# SimpleGif
An Android NDK GIF Library
========
GIF library built with cmake for usage with android gradle build system.
This c source code refer to android-ndk-gif.

========
Note
The next version will support bind the Context's lifecycle.

Blog
[http://blog.csdn.net/qq372848728/article/details/59104104](http://blog.csdn.net/qq372848728/article/details/59104104)

How do I use SimpleGif?
-------------------
The api of SimpleGif is similar to Glide,but SimpleGif is a NDK library.
Simple use cases will look something like this:

```java
// For a simple view:
@Override public void onCreate(Bundle savedInstanceState) {
  ...
  ImageView imageView = (ImageView) findViewById(R.id.my_image_view);

  SimpleGif.with(this).load("/sdcard/simple.gif").into(imageView);
}
```


Decode
========

![gif](https://github.com/yylyingy/ndkgif/blob/master/sample/src/main/assets/display.gif)

![gif](https://github.com/yylyingy/ndkgif/blob/master/sample/src/main/assets/sample1.gif)

Feature
========
* GIF Encoding.
* GIF Decoding.