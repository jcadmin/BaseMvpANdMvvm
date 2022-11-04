//
// Created by 49242 on 2022/11/3.
//

#ifndef BASEMVPANDMVVM_TRIANGLESAMPLE_H
#define BASEMVPANDMVVM_TRIANGLESAMPLE_H

#include <GLES3/gl3.h>

class TriangleSample {
public:
    TriangleSample();

    virtual ~TriangleSample();

    virtual void Init();

    virtual void Draw();

protected:
    GLuint m_VertexShader;
    GLuint m_FragmentShader;
    GLuint m_ProgramObj;
    int m_SurfaceWidth;
    int m_SurfaceHeight;
};


#endif //BASEMVPANDMVVM_TRIANGLESAMPLE_H
