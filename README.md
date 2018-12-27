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

BSD 3-Clause License

Copyright (c) 2018, malenkix
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of the copyright holder nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

## 3rd Party

* Java Advanced Imaging Image I/O Tools API core (standalone)
* (JJ2000) JPEG2000 support for Java Advanced Imaging Image I/O Tools API

Copyright 1994-2009 Sun Microsystems, Inc. All Rights Reserved.

Redistribution and use in source and binary forms, with or without 
modification, are permitted provided that the following conditions are met:

* Redistribution of source code must retain the above copyright notice, this 
  list of conditions and the following disclaimer.

* Redistribution in binary form must reproduce the above copyright notice, 
  this list of conditions and the following disclaimer in the documentation 
  and/or other materials provided with the distribution.

* Neither the name of Sun Microsystems, Inc. or the names of contributors 
  may be used to endorse or promote products derived from this software 
  without specific prior written permission.

This software is provided "AS IS," without a warranty of any kind. 
ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS 
SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR 
ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, 
INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, 
ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.

You acknowledge that this software is not designed, licensed or intended for use in the design, construction, operation or maintenance of any nuclear facility.

### List of dependencies and licsenses:

* (BSD 3-clause License w/nuclear disclaimer) Java Advanced Imaging Image I/O Tools API core (standalone) (com.github.jai-imageio:jai-imageio-core:1.4.0 - https://github.com/jai-imageio/jai-imageio-core)
* (BSD 3-clause License w/nuclear disclaimer) (JJ2000) JPEG2000 support for Java Advanced Imaging Image I/O Tools API (com.github.jai-imageio:jai-imageio-jpeg2000:1.3.0 - https://github.com/jai-imageio/jai-imageio-jpeg2000)
* (The Apache Software License, Version 2.0) Apache Commons Logging (commons-logging:commons-logging:1.2 - http://commons.apache.org/proper/commons-logging/)
* (The MIT License (MIT)) thumbnailator (net.coobird:thumbnailator:0.4.8 - http://code.google.com/p/thumbnailator)
* (The Apache Software License, Version 2.0) Apache FontBox (org.apache.pdfbox:fontbox:2.0.13 - http://pdfbox.apache.org/)
* (The Apache Software License, Version 2.0) PDFBox JBIG2 ImageIO plugin (org.apache.pdfbox:jbig2-imageio:3.0.2 - https://www.apache.org/jbig2-imageio/)
* (The Apache Software License, Version 2.0) Apache PDFBox (org.apache.pdfbox:pdfbox:2.0.13 - https://www.apache.org/pdfbox-parent/pdfbox/)
* (The Apache Software License, Version 2.0) Apache PDFBox Debugger (org.apache.pdfbox:pdfbox-debugger:2.0.13 - https://www.apache.org/pdfbox-parent/pdfbox-debugger/)
* (The Apache Software License, Version 2.0) Apache PDFBox tools (org.apache.pdfbox:pdfbox-tools:2.0.13 - https://www.apache.org/pdfbox-parent/pdfbox-tools/)
* (Bouncy Castle Licence) Bouncy Castle S/MIME API (org.bouncycastle:bcmail-jdk15on:1.60 - http://www.bouncycastle.org/java.html)
* (Bouncy Castle Licence) Bouncy Castle PKIX, CMS, EAC, TSP, PKCS, OCSP, CMP, and CRMF APIs (org.bouncycastle:bcpkix-jdk15on:1.60 - http://www.bouncycastle.org/java.html)
* (Bouncy Castle Licence) Bouncy Castle Provider (org.bouncycastle:bcprov-jdk15on:1.60 - http://www.bouncycastle.org/java.html)
* (The MIT License (MIT)) SLF4J API Module (org.slf4j:slf4j-api:1.7.25 - http://www.slf4j.org)
* (The MIT License (MIT)) SLF4J Simple Binding (org.slf4j:slf4j-simple:1.7.25 - http://www.slf4j.org)
