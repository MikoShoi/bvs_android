#ifndef HELLO_LIBS_MODELDATA_H
#define HELLO_LIBS_MODELDATA_H

#include <vector>

struct ModelData
{
    void clear();

    std::vector<int>    indices     {};
    std::vector<float>  posAndNorms {};
};


#endif //HELLO_LIBS_MODELDATA_H
