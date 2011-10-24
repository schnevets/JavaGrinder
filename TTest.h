#pragma once

#include <stdint.h>
#include <string>
#include <cstring>
#include "java_lang.h"

namespace oop {
	struct __TestTest;
	struct __TestTest_VT;

	typedef __TestTest* TestTest;
    typedef java::lang::Object Object;
    typedef java::lang::Class Class;
    typedef java::lang::String String;
    

	struct __TestTest {
		__TestTest_VT* __vptr;
        static String yesImSeriouslyDoingThis;
		
		__TestTest();
        

		static int32_t hashcode(TestTest);
		static bool equals(TestTest, Object);
		static Class getClass(TestTest);
		static String toString(TestTest);
		static void sayHi(TestTest);

		static Class __class();

		static __TestTest_VT __vtable;
	};

	struct __TestTest_VT {
		Class __isa;
		int32_t (*hashCode)(TestTest);
		bool (*equals)(TestTest, Object);
		Class (*getClass)(TestTest);
		String (*toString)(TestTest);

		__TestTest_VT()
		: __isa(__TestTest::__class()),
          hashCode((int32_t(*)(TestTest))&java::lang::__Object::hashCode),
		  equals((bool(*)(TestTest,Object))&java::lang::__Object::equals),
		  getClass((Class(*)(TestTest))&java::lang::__Object::getClass),
		  toString((String(*)(TestTest))&java::lang::__Object::toString){
		}
	};
}