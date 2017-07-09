#ifndef HELLO_LIBS_MODELLOADER_H
#define HELLO_LIBS_MODELLOADER_H

#include <string>
#include <iostream>

#include <postprocess.h>
#include <Importer.hpp>
#include <vector3.h>
#include <scene.h>
#include <mesh.h>
#include <jni.h>

#include "ModelData.h"

class ModelLoader
{
public:
    const ModelData & getModelData();
    ModelLoader& load(std::string modelFilePath);

private:
    void loadIndices    (aiMesh *aiMesh);
    void loadPosAndNorms(aiMesh *aiMesh);

    ModelData modelData{};
};


#endif //HELLO_LIBS_MODELLOADER_H
