#include <android/log.h>
#include "ModelLoader.h"
#define printf(...) __android_log_print(ANDROID_LOG_DEBUG, "TAG", __VA_ARGS__);

const ModelData & ModelLoader::getModelData()
{
    return modelData;
}
ModelLoader& ModelLoader::load(std::string modelFilePath)
{
    modelData.clear();

    Assimp::Importer importer {};
    const aiScene* scene = importer.ReadFile( modelFilePath,
                                              aiProcess_GenNormals            |
                                              aiProcess_JoinIdenticalVertices |
                                              aiProcess_Triangulate );
    if(!scene)
    {
        std::__throw_runtime_error("Can not import model data");
    }

    aiMesh *mesh = scene->mMeshes[0];
    loadPosAndNorms(mesh);
    loadIndices(mesh);

    return *this;
}
void ModelLoader::loadPosAndNorms (aiMesh  *mesh)
{
    if( !mesh->HasPositions() || !mesh->HasNormals())
    {
        std::__throw_runtime_error("Model does not contain pos or/and norms data");
    }

    int verticesNumber = mesh->mNumVertices;
    for(int i=0; i<verticesNumber; i++)
    {
        aiVector3D position { mesh->mVertices[i] };
//        aiVector3D normal   { mesh->mNormals [i] };

        modelData.posAndNorms.push_back(position.x);
        modelData.posAndNorms.push_back(position.y);
        modelData.posAndNorms.push_back(position.z);

//        modelData.posAndNorms.push_back(normal.x);
//        modelData.posAndNorms.push_back(normal.y);
//        modelData.posAndNorms.push_back(normal.z);
    }
}
void ModelLoader::loadIndices     (aiMesh  *mesh)
{
    if( !mesh->HasFaces() )
    {
        std::__throw_runtime_error("Model does not contain index data");
    }

    int facesNumber = mesh->mNumFaces;
    for(int i = 0; i<facesNumber; i++)
    {
        aiFace face{mesh->mFaces[i]};

        modelData.indices.push_back( face.mIndices[0] );
        modelData.indices.push_back( face.mIndices[1] );
        modelData.indices.push_back( face.mIndices[2] );
    }
}