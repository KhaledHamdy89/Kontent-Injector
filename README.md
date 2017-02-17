# Kontent-Injector

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

" Here is the $%$ClassA.MethodA$%$ from $%$ClassB.MethodB$%$ "

If we use this template with two classes:
ClassA
Having a method called MethodA that returns a string "AWESOME file"
and ClassB
Having a method called MethodB that returns a string "THE KONTENT-INJECTOR"

and send an object from ClassA, an object from ClassB and the template string/file to the Kontent-Injector, the file generated would look like this:

" Here is the AWESOME file from THE KONTENT-INJECTOR "

# What's special about the Kontent-Injector
1. You DON'T NEED to write any code no matter what template you use.
2. You can have loops/Conditional statements inside your template to decide the content that will be injected into the template!
3. Use the models you already have to inject values into the template without writing any code to get values from it, the Kontent-Injector will invoke the methods you put in the template.

# How to use it in details:
# Configurations:
# Classes Configurations:
# Loops:
# Conditions:
