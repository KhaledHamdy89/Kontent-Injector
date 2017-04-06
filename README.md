
# [Kontent-Injector](https://khaledhamdy89.github.io/Kontent-Injector/)

The Kontent-Injector is a library that allows developers to generate files from templates easily and hassle-free.
Instead of having tokens in a template to replace with your values, and write code to search for this specific token, or even seek a certain point in the template file and insert your content in, you simply use the Kontent-Injector:
  1. Write your template (How to write it will be addressed later).
  2. Call the Kontent-Injector's Inject values, and pass to it the objects holding the content and the template.
  3. Voilla! you get the generated file!
  
# How does this work?!
Simply, you send the template and the objects where you have the values in, and it generates the file.
Let's say you have two classes where you have data in and you need to generate a file from a template with this data.
You send both classes to the Kontent-Injector along with the template, that is written in a specific way, and it's really SIMPLE!
The template would look something like this:
```
" Here is the $%$ClassA.MethodA$%$ from $%$ClassB.MethodB$%$ "
```
If we use this template with two classes:
ClassA
Having a method called MethodA that returns a string "AWESOME file"
and ClassB
Having a method called MethodB that returns a string "THE KONTENT-INJECTOR"

and send an object from ClassA, an object from ClassB and the template string/file to the Kontent-Injector, the file generated would look like this:
```
" Here is the AWESOME file from THE KONTENT-INJECTOR "
```
# What's special about the Kontent-Injector
1. Quick to deploy. Quick to use. 
2. You DON'T NEED to write any code no matter what template you use.
3. You can have loops/Conditional statements inside your template to decide the content that will be injected into the template!
4. Use the models you already have to inject values into the template without writing any code to get values from it, the Kontent-Injector will invoke the methods you put in the template.
5. Everything is configurable.

# How to use it in details:
## Configurations:
The Kontent-Injector is fully configurable, and the KI configurations can be divided into two sections
### Templates Configurations:
The template configurations are related to how templates are written.
You can change:
1. The injection token, that is the set of symbols (or letters if desired) that indicates that there is an injection to be made, default symbols are "$%$" (Without the quotes). So if we were to change the default and make it "#" the template is written as follows:
```
The value is: #Class.Method#
```
2. The Loop start/end words, and those are two words indicating that the KI should treat the enclosed template as a loop (Loops are explained later in more details). Notice that the start and end words are enclosed with the injection token too. The default is "LOOP" and "ENDLOOP" for the start and end words respectively (Without the quotes). So an example for a loop can be as follows (With the injection token changed to "#"):
```
#LOOP# This is a loop for #Class.LoopMethod# ... #ENDLOOP#
```
### Classes Configurations:
Classes configurations are simply for protection and flexibility. The KI can let you define aliases for your classes, so you don't need to use your classes' names in the template, and the same goes for methods.
## Loops:
Loops allow you to have parts of your template repeated based on the passed content.
So for example if you have a list that contains the values "T1", "T2", "T3".

and you want to generate something that looks like that:
```
Element T1. Element T2. Element T3. 
```

your template will look something like this:
```
$%$LOOP$%$Element $%$MyClass.MyMethodReturningList$%$. $%$ENDLOOP$%$
```
And of course, remember you can use whatever you'd like as an injection token.

You can create a "Single line loop" or "Multiple Lines Loop"
To create a "Single Line Loop", simply have the Loop start and Loop end words on the same line, like so:

```
$%$LOOP$%$Element $%$MyClass.MyMethodReturningList$%$. $%$ENDLOOP$%$
```

and to have it generated on multiple lines, make the end loop word on the next line, like so:

```
$%$LOOP$%$Element $%$MyClass.MyMethodReturningList$%$. 
$%$ENDLOOP$%$
```
This will generate:
```
Element T1. 
Element T2. 
Element T3. 
```

## Conditions:
PLANNED


So Finally,
for the following template:
```
Let's test some injection!

We first start off by testing a single line injection
Awesome KInjector says: @@MockContentObject.methodReturnsString@@
now we test using a class alias and a method alias! @@Mock2.The_S_Method@@
now! A class Alias and no method alias @@Mock2.methodReturnsMockContentObject@@
For our big finale! LOOOOOOOOOPS

Single Line: @@LOOP@@element: @@MockContentObject.methodReturnStringList@@ @@ENDLOOP@@
AND MULTILINE!! @@LOOP@@here you go... @@Mock2.methodReturnStringList@@ ...
repeat!! @@ENDLOOP@@
Report...
Everything is AWESOME!

```

With a single line of code!
```
KontentInjector kinjector = new KontentInjector();
kinjector.injectValues(input, output, new MockContentObject(), new MockContentObject2());
```

This will be GENERATED!

```
Let's test some injection!

We first start off by testing a single line injection
Awesome KInjector says: STRING INJECTION
now we test using a class alias and a method alias! STRING INJECTION V2
now! A class Alias and no method alias OBJECT INJECTION V2
For our big finale! LOOOOOOOOOPS

Single Line: element: 1 element: 2 element: 3 element: Cool
AND MULTILINE!! here you go... 1 V2 ...
repeat!! here you go... 2 V2 ...
repeat!! here you go... 3 V2 ...
repeat!! here you go... Cool V2 ...
repeat!!
Report...
Everything is AWESOME!

```