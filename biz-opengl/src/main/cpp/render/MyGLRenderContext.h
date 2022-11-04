#ifndef BASEMVPANDMVVM_MYGLRENDERCONTEXT_H
#define BASEMVPANDMVVM_MYGLRENDERCONTEXT_H

#include "stdint.h"
#include <GLES3/gl3.h>
#include "TriangleSample.h"

class MyGLRenderContext {
    MyGLRenderContext();

    ~MyGLRenderContext();

public:
    void SetImageData(int format, int width, int height, uint8_t *pData);

    void OnSurfaceCreated();

    void OnSurfaceChanged(int width, int height);

    void OnDrawFrame();

    static MyGLRenderContext *GetInstance();

    static void DestroyInstance();

private:
    static MyGLRenderContext *m_pContext;
    int m_ScreenW;
    int m_ScreenH;
    TriangleSample *m_Sample;
};


#endif
