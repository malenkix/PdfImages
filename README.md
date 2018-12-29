# PdfImages

Ever bought a PDF file containing large background images (e.g. character sheets for role-playing games) making it hard to read the file on a mobile device or to print it and not be able to find software to remove these images for free?
Maybe you found something to remove the background image just to notice the whole page has been created with a colored background?

I don't know if you ever faced these problems but I did and created this piece of Java software to my rescue.
Feel free to check it out (Java 8 JRE needed) and download the runnable jar from the release section.

## Manual

Go to `File` and choose `Open File...` to select and open a pdf file from your computer.
The pages and images will be loaded and shown in the UI. Wait until all pages and images have been loaded and the progress bar is at 100%.

You can choose a page or image with your mouse and navigate between them using your keyboard as well.

If you selected a page you can check `Remove Page` to exclude it from your final file. Please note all images belonging to the page will be highlighted as well and won't allow any editing anymore.

If you selected an image you can choose between different modes of what should be done with this image in the final file:
* Select `Set Blank` to replace it with a 1x1 transparent pixel.
* Select `Set White` to replace it with a white image having the same size.
* Select `Set Color...` to choose a different color as white.

You can press the button `Apply On Same Images` if you want to apply your selection to all same looking images.
Please note the focus will be set to the images afterwards for your convenience.

When you are happy with your changes you can go to `File` and choose `Save File` or `Save File As...` to save your changes.
If you already saved before the previous chosen file will be remembered and you can check it hovering `Save File` to view it.

## License

MIT

Copyright (c) 2018 malenkix

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files 
(the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, 
publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do 
so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 

## 3rd Party

* (The Apache Software License, Version 2.0) Apache Commons Logging (commons-logging:commons-logging:1.2 - http://commons.apache.org/proper/commons-logging/)
* (The MIT License (MIT)) thumbnailator (net.coobird:thumbnailator:0.4.8 - http://code.google.com/p/thumbnailator)
* (The Apache Software License, Version 2.0) Apache FontBox (org.apache.pdfbox:fontbox:2.0.13 - http://pdfbox.apache.org/)
* (The Apache Software License, Version 2.0) PDFBox JBIG2 ImageIO plugin (org.apache.pdfbox:jbig2-imageio:3.0.2 - https://www.apache.org/jbig2-imageio/)
* (The Apache Software License, Version 2.0) Apache PDFBox (org.apache.pdfbox:pdfbox:2.0.13 - https://www.apache.org/pdfbox-parent/pdfbox/)
* (The Apache Software License, Version 2.0) Apache PDFBox Debugger (org.apache.pdfbox:pdfbox-debugger:2.0.13 - https://www.apache.org/pdfbox-parent/pdfbox-debugger/)
* (Bouncy Castle Licence) Bouncy Castle S/MIME API (org.bouncycastle:bcmail-jdk15on:1.60 - http://www.bouncycastle.org/java.html)
* (Bouncy Castle Licence) Bouncy Castle PKIX, CMS, EAC, TSP, PKCS, OCSP, CMP, and CRMF APIs (org.bouncycastle:bcpkix-jdk15on:1.60 - http://www.bouncycastle.org/java.html)
* (Bouncy Castle Licence) Bouncy Castle Provider (org.bouncycastle:bcprov-jdk15on:1.60 - http://www.bouncycastle.org/java.html)
