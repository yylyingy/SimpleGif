#pragma once

#include <stdint.h>

class GifFrame
{
public:
	GifFrame(uint32_t* data, int32_t delayMs);
	~GifFrame(void);

	uint32_t* data;
	int32_t delayMs;
};

