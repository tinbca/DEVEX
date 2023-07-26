package com.Devex.Utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Devex.Entity.ImageProduct;
import com.Devex.Sevice.ImageProductService;

@Service
public class FileManagerService {
    @Value("${myapp.file-storage-path}")
    private String fileStoragePath;

    @Autowired
    ImageProductService imageProductService;

    private Path getPath(String shopname, String id, String filename) {
    	File shop = Paths.get(fileStoragePath, shopname).toFile();
    	if(!shop.exists()) {
    		shop.mkdirs();
    	}
        File dir = Paths.get(fileStoragePath, shopname, id).toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return Paths.get(dir.getAbsolutePath(), filename);
    }

    public byte[] read(String shopname, String id, String filename) {
        Path path = this.getPath(shopname, id, filename);
        try {
            return Files.readAllBytes(path);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public List<String> save(String shopname, String id, MultipartFile[] files) {
        List<String> filenames = new ArrayList<String>();
        for (MultipartFile file : files) {
            String name = System.currentTimeMillis() + file.getOriginalFilename();
            String filename = Integer.toHexString(name.hashCode()) + name.substring(name.lastIndexOf("."));
            Path path = this.getPath(shopname, id, filename);
            try {
                file.transferTo(path);
                filenames.add(filename);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filenames;
    }

    public void delete(String shopname, String id, String filename) {
        Path path = this.getPath(shopname, id, filename);
        path.toFile().delete();
    }

    public List<String> list(String id, String shopname) {
    	int index = 0;
        List<ImageProduct> listImageProduct = imageProductService.findAllImageProductByProductId(id);
        List<String> filenames = new ArrayList<String>();
        File dir = Paths.get(fileStoragePath, shopname, id).toFile();
        File shop = Paths.get(fileStoragePath, shopname).toFile();
        if(!shop.exists()) {
        	shop.mkdirs();
        	if(!dir.exists()){
                dir.mkdirs();
                File list = Paths.get(fileStoragePath).toFile();
                File[] files = list.listFiles();
                for (File file : files) {
                	for (ImageProduct imageProduct : listImageProduct) {
						if(file.getName().equalsIgnoreCase(imageProduct.getName())) {
				            try {
				            	Path source = file.toPath();
				            	Path target = this.getPath(shopname, id, file.getName());
				            	Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
				                filenames.add(file.getName());
				                index++;
				                System.out.println(index);
				                if(index > 1) {
				                	Path delete = source;
				                	delete.toFile().delete();
				                }
				            } catch (Exception e) {
				                e.printStackTrace();
				            }
							filenames.add("/img/product/"+file.getName());
		                    System.out.println("có file có list");
						}
					}
                }
            }
        }else {
        	if (dir.exists() && listImageProduct != null) {
                File[] files = dir.listFiles();
                for (File file : files) {
                    filenames.add("/img/product/"+file.getName());
                    System.out.println("có file có list");
                }
            } else if (dir.exists() && listImageProduct == null) {
                File file = Paths.get(fileStoragePath, shopname).toFile();
                Path path = Paths.get(file.getAbsolutePath(), id);
                path.toFile().delete();
                System.out.println("khong file khong list");
            }else {
            	if(!dir.exists()){
                    dir.mkdirs();
                    File list = Paths.get(fileStoragePath).toFile();
                    File[] files = list.listFiles();
                    for (File file : files) {
                    	for (ImageProduct imageProduct : listImageProduct) {
    						if(file.getName().equalsIgnoreCase(imageProduct.getName())) {
    				            try {
    				            	Path source = file.toPath();
    				            	Path target = this.getPath(shopname, id, file.getName());
    				            	Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    				                filenames.add(file.getName());
    				                index++;
    				                System.out.println(index);
    				                if(index > 1) {
    				                	Path delete = source;
    				                	delete.toFile().delete();
    				                }
    				            } catch (Exception e) {
    				                e.printStackTrace();
    				            }
    							filenames.add("/img/product/"+file.getName());
    		                    System.out.println("có file có list");
    						}
    					}
                    }
                }
            }
        }
        return filenames;
    }
}
