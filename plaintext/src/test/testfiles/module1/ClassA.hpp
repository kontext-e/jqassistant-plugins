//
// some header comment
//

#ifndef MODULE_1_CLASSA_H
#define MODULE_1_CLASSA_H

#include <memory>
#include <vector>

#include "IncludeFromOwnModule.hpp"
#include "../module2/ClassB.hpp"

using namespace std;

class ClassA {
private:
    int field1;

public:
    void setField1(const int field1);
};


#endif //MODULE_A_CLASSA_H
