import os
import re

def duplicate_and_rename_classes(root_dir, target_name="Employee", replacement_name="Worker"):
    """
    Duplicates all Java classes containing the target_name and replaces
    target_name with replacement_name in their names and content.

    Args:
        root_dir (str): The root directory of the project.
        target_name (str): The name to search for in class names and content.
        replacement_name (str): The name to replace the target_name with.
    """
    for subdir, _, files in os.walk(root_dir):
        for file in files:
            if file.endswith(".java") and re.search(target_name, file, re.IGNORECASE):
                original_file_path = os.path.join(subdir, file)
                with open(original_file_path, "r", encoding="utf-8") as f:
                    content = f.read()

                # Perform case-insensitive replacements in content
                updated_content = content
                updated_content = re.sub(rf'\b{re.escape(target_name)}\w*', lambda m: replacement_name + m.group(0)[len(target_name):], updated_content)
                updated_content = re.sub(rf'\b{re.escape(target_name)}\b', replacement_name, updated_content, flags=re.IGNORECASE)
                updated_content = updated_content.replace('employees', 'workers').replace('EMPLOYEE', 'WORKER').replace('Employee', 'Worker')

                # Create a new file with the updated name
                new_file_name = re.sub(rf'{re.escape(target_name)}', replacement_name, file, flags=re.IGNORECASE)
                new_file_path = os.path.join(subdir, new_file_name)

                with open(new_file_path, "w", encoding="utf-8") as f:
                    f.write(updated_content)

                print(f"Duplicated and renamed: {original_file_path} -> {new_file_path}")

# Example usage
root_directory = "C:\spring-templates\spring-core\src"  # Replace with your project's root directory
duplicate_and_rename_classes(root_directory, target_name="Employee", replacement_name="Worker")
