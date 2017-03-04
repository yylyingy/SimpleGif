LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := androidndkgif
LOCAL_SRC_FILES := DataBlock.cpp BitWritingBlock.cpp GifDecoder.cpp GifEncoder.cpp GifFrame.cpp io_github_yylyingy_gifencodedecode_GifDecoder.cpp io_github_yylyingy_gifencodedecode_GifEncoder.cpp GCTGifEncoder.cpp LCTGifEncoder.cpp SimpleGCTEncoder.cpp BaseGifEncoder.cpp
LOCAL_LDFLAGS += -ljnigraphics
include $(BUILD_SHARED_LIBRARY)

