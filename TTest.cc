#include "java_lang.h"

#include "TTest.h"
#include <iostream>
#include <string>

namespace oop{
	__TestTest::__TestTest() : __vptr(&__vtable) {
        std::string yesImSeriouslyDoingThis_s = "Hi Waldo";
		yesImSeriouslyDoingThis = new String(yesImSeriouslyDoingThis_s);
	}
	
	void __TestTest::sayHi(TestTest __this) {
		std::cout << yesImSeriouslyDoingThis;
	}
}